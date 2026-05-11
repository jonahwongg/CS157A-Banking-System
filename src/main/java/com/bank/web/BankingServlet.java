package com.bank.web;

import com.bank.service.BankingService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

@WebServlet("/banking")
public class BankingServlet extends BaseServlet {
    private final BankingService bankingService = new BankingService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String description = request.getParameter("description");

        try {
            if ("deposit".equals(action)) {
                bankingService.deposit(
                        Integer.parseInt(request.getParameter("accountId")),
                        new BigDecimal(request.getParameter("amount")),
                        description
                );
                redirectWithMessage(request, response, "success", "Deposit completed.");
            } else if ("withdraw".equals(action)) {
                bankingService.withdraw(
                        Integer.parseInt(request.getParameter("accountId")),
                        new BigDecimal(request.getParameter("amount")),
                        description
                );
                redirectWithMessage(request, response, "success", "Withdrawal completed.");
            } else if ("transfer".equals(action)) {
                bankingService.transfer(
                        Integer.parseInt(request.getParameter("fromAccountId")),
                        Integer.parseInt(request.getParameter("toAccountId")),
                        new BigDecimal(request.getParameter("amount")),
                        description
                );
                redirectWithMessage(request, response, "success", "Transfer completed.");
            } else {
                redirectWithMessage(request, response, "error", "Unknown banking action.");
            }
        } catch (SQLException | IllegalArgumentException ex) {
            redirectWithMessage(request, response, "error", ex.getMessage());
        }
    }
}
