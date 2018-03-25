package ch.frostnova.mimic.servlet;

import ch.frostnova.mimic.api.WebRequest;
import ch.frostnova.mimic.api.WebResponse;
import ch.frostnova.mimic.api.type.RequestMethod;
import ch.frostnova.mimic.service.engine.MimicEngine;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Mimic dispatcher servlet, dispatches requests to matching mimic mappings.
 *
 * @author pwalser
 * @since 23.01.2018.
 */
@WebServlet(urlPatterns = "/*", loadOnStartup = 1, displayName = "Mimic Dispatcher")
@MultipartConfig
public class MimicDispatcherServlet extends HttpServlet {

    @Autowired
    private Logger logger;

    @Autowired
    private MimicEngine mimicEngine;

    private void dispatch(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        req.getSession().setAttribute("time", LocalDateTime.now());

        logger.debug("dispatch: " + req.getMethod() + " " + req.getRequestURI());

        WebRequest webRequest = new ServletWebRequest(req);
        WebResponse webResponse = mimicEngine.eval(webRequest);

        //TODO: validate webResponse (Status codes, content type format)
        //TODO: interpret redirects

        resp.setStatus(webResponse.getStatus());
        resp.setContentType(webResponse.getContentType());
        resp.setCharacterEncoding(StandardCharsets.UTF_8.displayName());

        Map<String, String> headers = webResponse.getHeaders();
        if (headers != null) {
            headers.forEach(resp::setHeader);
        }

        byte[] body = webResponse.getBody();
        if (body != null) {
            resp.setContentLength(body.length);
            resp.getOutputStream().write(body);
        } else {
            resp.setContentLength(0);
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (req.getMethod().equalsIgnoreCase("GET") || req.getMethod().equalsIgnoreCase("HEAD")) {
            // dispatch over HttpServlet (sets last modified stuff, which we don't want to reimplement)
            super.service(req, resp);
        } else {
            boolean supportedRequestMethod = RequestMethod.resolve(req.getMethod()).isPresent();
            if (supportedRequestMethod) {
                dispatch(req, resp);
            } else {
                resp.setStatus(405);
                resp.getWriter().print("Unsupported method: " + req.getMethod());
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        dispatch(req, resp);
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatch(req, resp);
    }
}
