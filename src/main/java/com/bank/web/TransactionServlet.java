package com.bank.web;

import com.bank.service.TransactionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/transactions")
public class TransactionServlet extends BaseServlet {
    private final TransactionService transactionService = new TransactionService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            transactionService.deleteTransaction(Integer.parseInt(request.getParameter("transactionId")));
            redirectWithMessage(request, response, "success", "Transaction removed from the log.");
        } catch (SQLException | IllegalArgumentException ex) {
            redirectWithMessage(request, response, "error", ex.getMessage());
        }
    }
}
