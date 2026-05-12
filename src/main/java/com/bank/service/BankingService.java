package com.bank.service;

import com.bank.dao.AccountDao;
import com.bank.dao.TransactionDao;
import com.bank.model.Account;
import com.bank.model.BankTransaction;
import com.bank.util.ConnectionFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

public class BankingService {
    private static final int SYSTEM_ACCOUNT_ID = 1;

    private final AccountDao accountDao = new AccountDao();
    private final TransactionDao transactionDao = new TransactionDao();

    public void deposit(int accountId, BigDecimal amount, String description) throws SQLException {
        ensurePositiveAmount(amount);
        try (Connection connection = ConnectionFactory.getConnection()) {
            connection.setAutoCommit(false);
            try {
                Account account = requireActiveAccount(connection, accountId);
                BigDecimal newBalance = account.getBalance().add(amount);
                accountDao.updateBalance(connection, accountId, newBalance);

                BankTransaction transaction = new BankTransaction();
                transaction.setFromAccountId(SYSTEM_ACCOUNT_ID);
                transaction.setToAccountId(accountId);
                transaction.setTransactionType("DEPOSIT");
                transaction.setAmount(amount);
                transaction.setDescription(description);
                transactionDao.create(connection, transaction);

                connection.commit();
            } catch (SQLException | IllegalArgumentException ex) {
                connection.rollback();
                throw ex;
            }
        }
    }

    public void withdraw(int accountId, BigDecimal amount, String description) throws SQLException {
        ensurePositiveAmount(amount);
        try (Connection connection = ConnectionFactory.getConnection()) {
            connection.setAutoCommit(false);
            try {
                Account account = requireActiveAccount(connection, accountId);
                ensureSufficientFunds(account, amount);

                BigDecimal newBalance = account.getBalance().subtract(amount);
                accountDao.updateBalance(connection, accountId, newBalance);

                BankTransaction transaction = new BankTransaction();
                transaction.setFromAccountId(accountId);
                transaction.setToAccountId(SYSTEM_ACCOUNT_ID);
                transaction.setTransactionType("WITHDRAWAL");
                transaction.setAmount(amount);
                transaction.setDescription(description);
                transactionDao.create(connection, transaction);

                connection.commit();
            } catch (SQLException | IllegalArgumentException ex) {
                connection.rollback();
                throw ex;
            }
        }
    }

    public void transfer(int fromAccountId, int toAccountId, BigDecimal amount, String description) throws SQLException {
        ensurePositiveAmount(amount);
        if (fromAccountId == toAccountId) {
            throw new IllegalArgumentException("Source and destination accounts must be different.");
        }

        try (Connection connection = ConnectionFactory.getConnection()) {
            connection.setAutoCommit(false);
            try {
                Account fromAccount = requireActiveAccount(connection, fromAccountId);
                Account toAccount = requireActiveAccount(connection, toAccountId);
                ensureSufficientFunds(fromAccount, amount);

                accountDao.updateBalance(connection, fromAccountId, fromAccount.getBalance().subtract(amount));
                accountDao.updateBalance(connection, toAccountId, toAccount.getBalance().add(amount));

                BankTransaction transaction = new BankTransaction();
                transaction.setFromAccountId(fromAccountId);
                transaction.setToAccountId(toAccountId);
                transaction.setTransactionType("TRANSFER");
                transaction.setAmount(amount);
                transaction.setDescription(description);
                transactionDao.create(connection, transaction);

                connection.commit();
            } catch (SQLException | IllegalArgumentException ex) {
                connection.rollback();
                throw ex;
            }
        }
    }

    private Account requireActiveAccount(Connection connection, int accountId) throws SQLException {
        Account account = accountDao.findById(connection, accountId);
        if (account == null) {
            throw new IllegalArgumentException("Account " + accountId + " does not exist.");
        }
        if (!"ACTIVE".equalsIgnoreCase(account.getStatus())) {
            throw new IllegalArgumentException("Account " + account.getAccountNumber() + " is not active.");
        }
        return account;
    }

    private void ensureSufficientFunds(Account account, BigDecimal amount) {
        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance for account " + account.getAccountNumber() + ".");
        }
    }

    private void ensurePositiveAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
    }
}
