package com.bank.service;

import com.bank.dao.LoanDao;
import com.bank.model.Loan;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class LoanService {
    private final LoanDao loanDao = new LoanDao();

    public List<Loan> getAllLoans() throws SQLException {
        return loanDao.findAll();
    }

    public void createLoan(Loan loan) throws SQLException {
        if (loan.getApplicationDate() == null) {
            loan.setApplicationDate(Date.valueOf(LocalDate.now()));
        }
        loanDao.create(loan);
    }

    public void updateLoan(Loan loan) throws SQLException {
        loanDao.update(loan);
    }

    public void deleteLoan(int loanId) throws SQLException {
        loanDao.delete(loanId);
    }

    public void processLoanStatus(int loanId, Loan updatedLoan) throws SQLException {
        updatedLoan.setLoanId(loanId);
        loanDao.update(updatedLoan);
    }
}
