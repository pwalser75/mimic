package ch.frostnova.mimic.servlet;

import ch.frostnova.mimic.api.RuleEngine;
import ch.frostnova.mimic.api.WebRequest;
import ch.frostnova.mimic.api.WebResponse;
import ch.frostnova.mimic.api.type.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author pwalser
 * @since 23.01.2018.
 */
@WebServlet(urlPatterns = "/*")
public class MimicDispatcherServlet extends HttpServlet {

    private void dispatch(RequestMethod method, HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        WebRequest webRequest = new ServletWebRequest(req);
        RuleEngine ruleEngine = new RuleEngine();
        WebResponse webResponse = ruleEngine.eval(webRequest);

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
        dispatch(RequestMethod.GET, req, resp);
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatch(RequestMethod.HEAD, req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatch(RequestMethod.POST, req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatch(RequestMethod.PUT, req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatch(RequestMethod.DELETE, req, resp);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatch(RequestMethod.OPTIONS, req, resp);
    }

    @Override
    protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        dispatch(RequestMethod.TRACE, req, resp);
    }
}
