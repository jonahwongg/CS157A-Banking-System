package com.bank.web;

import com.bank.service.DashboardService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/dashboard")
public class DashboardServlet extends BaseServlet {
    private final DashboardService dashboardService = new DashboardService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);
            String customerQuery = request.getParameter("customerQuery");
            String accountQuery = request.getParameter("accountQuery");
            String transactionQuery = request.getParameter("transactionQuery");

            request.setAttribute("authUser", session == null ? null : session.getAttribute("authUser"));
            request.setAttribute("customerQuery", customerQuery);
            request.setAttribute("accountQuery", accountQuery);
            request.setAttribute("transactionQuery", transactionQuery);
            request.setAttribute("customers", dashboardService.getCustomers(customerQuery));
            request.setAttribute("accounts", dashboardService.getAccounts(accountQuery));
            request.setAttribute("transactions", dashboardService.getTransactions(transactionQuery));
            request.setAttribute("loans", dashboardService.getLoans());
            request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
        } catch (SQLException ex) {
            throw new ServletException("Unable to load dashboard data.", ex);
        }
    }
}
