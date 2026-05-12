package com.bank.web;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public abstract class BaseServlet extends HttpServlet {
    protected void redirectWithMessage(HttpServletRequest request, HttpServletResponse response, String type, String message)
            throws IOException {
        String encodedType = URLEncoder.encode(type, StandardCharsets.UTF_8);
        String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);
        response.sendRedirect(request.getContextPath() + "/dashboard?messageType=" + encodedType + "&message=" + encodedMessage);
    }
}
