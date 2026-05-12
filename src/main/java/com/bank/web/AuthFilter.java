package com.bank.web;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(urlPatterns = {"/*"})
public class AuthFilter implements Filter {
    @Override
    public void doFilter(jakarta.servlet.ServletRequest request, jakarta.servlet.ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String contextPath = httpRequest.getContextPath();
        String requestUri = httpRequest.getRequestURI();
        String path = requestUri.substring(contextPath.length());

        boolean publicPath =
                "/login".equals(path) ||
                "/index.jsp".equals(path) ||
                path.startsWith("/assets/");

        HttpSession session = httpRequest.getSession(false);
        boolean authenticated = session != null && session.getAttribute("authUser") != null;

        if (authenticated || publicPath) {
            chain.doFilter(request, response);
            return;
        }

        httpResponse.sendRedirect(contextPath + "/login");
    }
}
