package com.bank.web;

import com.bank.model.Account;
import com.bank.service.AccountService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

@WebServlet("/accounts")
public class AccountServlet extends BaseServlet {
    private final AccountService accountService = new AccountService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            if ("create".equals(action)) {
                accountService.createAccount(buildAccount(request, false));
                redirectWithMessage(request, response, "success", "Account created successfully.");
            } else if ("update".equals(action)) {
                accountService.updateAccount(buildAccount(request, true));
                redirectWithMessage(request, response, "success", "Account updated successfully.");
            } else if ("delete".equals(action)) {
                accountService.deleteAccount(Integer.parseInt(request.getParameter("accountId")));
                redirectWithMessage(request, response, "success", "Account deleted successfully.");
            } else {
                redirectWithMessage(request, response, "error", "Unknown account action.");
            }
        } catch (SQLException | IllegalArgumentException ex) {
            redirectWithMessage(request, response, "error", ex.getMessage());
        }
    }

    private Account buildAccount(HttpServletRequest request, boolean includeId) {
        Account account = new Account();
        if (includeId) {
            account.setAccountId(Integer.parseInt(request.getParameter("accountId")));
        }
        account.setCustomerId(Integer.parseInt(request.getParameter("customerId")));
        account.setAccountNumber(request.getParameter("accountNumber"));
        account.setAccountType(request.getParameter("accountType"));
        account.setBalance(new BigDecimal(request.getParameter("balance")));
        account.setStatus(request.getParameter("status"));
        return account;
    }
}
