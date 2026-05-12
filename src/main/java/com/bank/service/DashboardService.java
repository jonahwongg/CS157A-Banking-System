package com.bank.service;

import com.bank.model.Account;
import com.bank.model.BankTransaction;
import com.bank.model.Customer;
import com.bank.model.Loan;

import java.sql.SQLException;
import java.util.List;

public class DashboardService {
    private final CustomerService customerService = new CustomerService();
    private final AccountService accountService = new AccountService();
    private final TransactionService transactionService = new TransactionService();
    private final LoanService loanService = new LoanService();

    public List<Customer> getCustomers(String query) throws SQLException {
        return customerService.searchCustomers(query);
    }

    public List<Customer> getAllCustomers() throws SQLException {
        return customerService.getAllCustomers();
    }

    public List<Account> getAccounts(String query) throws SQLException {
        return accountService.searchAccounts(query);
    }

    public List<BankTransaction> getTransactions(String query) throws SQLException {
        return transactionService.searchTransactions(query);
    }

    public List<Loan> getLoans() throws SQLException {
        return loanService.getAllLoans();
    }
}
