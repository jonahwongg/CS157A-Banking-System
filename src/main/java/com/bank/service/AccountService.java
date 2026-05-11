package com.bank.service;

import com.bank.dao.AccountDao;
import com.bank.model.Account;

import java.sql.SQLException;
import java.util.List;

public class AccountService {
    private static final int SYSTEM_ACCOUNT_ID = 1;

    private final AccountDao accountDao = new AccountDao();

    public List<Account> getAllAccounts() throws SQLException {
        return accountDao.findAll();
    }

    public List<Account> searchAccounts(String query) throws SQLException {
        if (query == null || query.isBlank()) {
            return accountDao.findAll();
        }
        return accountDao.search(query);
    }

    public void createAccount(Account account) throws SQLException {
        accountDao.create(account);
    }

    public void updateAccount(Account account) throws SQLException {
        accountDao.update(account);
    }

    public void deleteAccount(int accountId) throws SQLException {
        if (accountId == SYSTEM_ACCOUNT_ID) {
            throw new IllegalArgumentException("The system reserve account cannot be deleted.");
        }
        accountDao.delete(accountId);
    }
}
