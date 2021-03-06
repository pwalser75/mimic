package ch.frostnova.mimic.servlet;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;

/**
 * Serve static resources.
 *
 * @author pwalser
 * @since 25.03.2018
 */
@WebServlet(urlPatterns = "/mimic/*", loadOnStartup = 1, displayName = "Static Resources")
@MultipartConfig
public class StaticResourcesServlet extends HttpServlet {

    private final static String RESOURCE_ROOT = "/static";

    @Autowired
    private Logger logger;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String requestURI = req.getRequestURI();
        logger.trace("request: " + requestURI);

        String relativePath = requestURI.substring(req.getServletPath().length());

        if (relativePath.equals("") || relativePath.equals("/")) {
            String redirectPath = req.getServletPath() + "/index.html ";
            logger.debug("Redirecting to: " + redirectPath);
            resp.sendRedirect(redirectPath);
            return;
        }

        String resourcePath = RESOURCE_ROOT + relativePath;

        try (InputStream in = getClass().getResourceAsStream(resourcePath)) {
            if (in == null) {
                logger.debug("Resource not found: " + resourcePath);
                resp.setStatus(404);
                resp.setContentType("text/plain");
                resp.getWriter().write("Resource not found: " + resourcePath);
                return;
            }

            String contentType = URLConnection.guessContentTypeFromName(relativePath);

            resp.setStatus(200);
            if (contentType != null) {
                resp.setContentType(contentType);
            }
            transfer(in, resp.getOutputStream());
        }
    }

    private void transfer(InputStream source, OutputStream target) throws IOException {

        BufferedInputStream in = new BufferedInputStream(source);
        BufferedOutputStream out = new BufferedOutputStream(target);
        byte[] buffer = new byte[0xFFF];
        int read;
        while ((read = in.read(buffer)) >= 0) {
            out.write(buffer, 0, read);
        }
        out.flush();
    }
}
