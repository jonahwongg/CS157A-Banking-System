package com.bank.web;

import com.bank.model.Loan;
import com.bank.service.LoanService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;

@WebServlet("/loans")
public class LoanServlet extends BaseServlet {
    private final LoanService loanService = new LoanService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            if ("create".equals(action)) {
                loanService.createLoan(buildLoan(request, false));
                redirectWithMessage(request, response, "success", "Loan application created.");
            } else if ("update".equals(action) || "process".equals(action)) {
                loanService.updateLoan(buildLoan(request, true));
                redirectWithMessage(request, response, "success", "Loan updated successfully.");
            } else if ("delete".equals(action)) {
                loanService.deleteLoan(Integer.parseInt(request.getParameter("loanId")));
                redirectWithMessage(request, response, "success", "Loan deleted successfully.");
            } else {
                redirectWithMessage(request, response, "error", "Unknown loan action.");
            }
        } catch (SQLException | IllegalArgumentException ex) {
            redirectWithMessage(request, response, "error", ex.getMessage());
        }
    }

    private Loan buildLoan(HttpServletRequest request, boolean includeId) {
        Loan loan = new Loan();
        if (includeId) {
            loan.setLoanId(Integer.parseInt(request.getParameter("loanId")));
        }
        loan.setCustomerId(Integer.parseInt(request.getParameter("customerId")));
        loan.setPrincipalAmount(new BigDecimal(request.getParameter("principalAmount")));
        loan.setInterestRate(new BigDecimal(request.getParameter("interestRate")));
        loan.setTermMonths(Integer.parseInt(request.getParameter("termMonths")));
        loan.setStatus(request.getParameter("status"));
        loan.setApplicationDate(Date.valueOf(request.getParameter("applicationDate")));
        return loan;
    }
}
