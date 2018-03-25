package ch.frostnova.mimic.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

//@WebFilter(urlPatterns = "/*")
public class StaticResourceFilter implements Filter {

    private final static Logger logger = LoggerFactory.getLogger(StaticResourceFilter.class);

    private static final String RESOURCE_PATH = "/static/";

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        logger.info("StaticResourceFilter initialized");
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response,
                         final FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = ((HttpServletRequest) request).getRequestURI();
        logger.warn("FILTER: " + requestURI);
        if (requestURI.toLowerCase().startsWith(RESOURCE_PATH)) {
            request.getRequestDispatcher(httpRequest.getServletPath()).forward(request, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        logger.info("StaticResourceFilter destroyed");
    }
}