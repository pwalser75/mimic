package ch.frostnova.mimic.servlet;

import ch.frostnova.mimic.api.WebRequest;
import ch.frostnova.mimic.api.WebResponse;
import ch.frostnova.mimic.engine.MimicEngine;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * Mimic dispatcher servlet, dispatches requests to matching mimic rules.
 *
 * @author pwalser
 * @since 23.01.2018.
 */
@WebServlet(urlPatterns = "/*", loadOnStartup = 1)
public class MimicDispatcherServlet extends HttpServlet {

    @Autowired
    private Logger logger;

    @Autowired
    private MimicEngine mimicEngine;

    private void dispatch(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.getSession().setAttribute("time", LocalDateTime.now());

        logger.debug("dispatch: " + req.getRequestURI());

        WebRequest webRequest = new ServletWebRequest(req);
        WebResponse webResponse = mimicEngine.eval(webRequest);

        //TODO: validate webResponse (Status codes, content type format)
        //TODO: interpret redirects

        resp.setContentType(webResponse.getContentType());
        resp.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
        byte[] body = webResponse.getBody();
        if (body != null) {
            resp.setContentLength(body.length);
            resp.getOutputStream().write(body);
        } else {
            resp.setContentLength(0);
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatch(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatch(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatch(req, resp);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatch(req, resp);
    }

    @Override
    protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatch(req, resp);
    }
}
