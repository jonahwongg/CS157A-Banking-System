package com.bank.service;

import com.bank.dao.AccountDao;
import com.bank.model.Account;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class AccountService {
    private static final int SYSTEM_ACCOUNT_ID = 1;
    private static final Pattern ACCOUNT_NUMBER_PATTERN = Pattern.compile("^(CHK|SVG|SYS|BUS)-\\d{5}$");

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
        validateAccount(account);
        accountDao.create(account);
    }

    public void updateAccount(Account account) throws SQLException {
        validateAccount(account);
        accountDao.update(account);
    }

    public void deleteAccount(int accountId) throws SQLException {
        if (accountId == SYSTEM_ACCOUNT_ID) {
            throw new IllegalArgumentException("The system reserve account cannot be deleted.");
        }
        accountDao.delete(accountId);
    }

    private void validateAccount(Account account) {
        if (account.getAccountNumber() == null || !ACCOUNT_NUMBER_PATTERN.matcher(account.getAccountNumber().trim()).matches()) {
            throw new IllegalArgumentException(
                    "Account number must use the format CHK-00001, SVG-00001, SYS-00001, or BUS-00001."
            );
        }
    }
}
