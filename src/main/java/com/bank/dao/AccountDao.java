package com.bank.dao;

import com.bank.model.Account;
import com.bank.util.ConnectionFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDao {
    public List<Account> findAll() throws SQLException {
        // SELECT query that joins customers so the UI can show account ownership details.
        String sql = """
                SELECT a.account_id, a.customer_id, a.account_number, a.account_type, a.balance, a.status,
                       CONCAT(c.first_name, ' ', c.last_name) AS customer_name
                FROM accounts a
                JOIN customers c ON c.customer_id = a.customer_id
                ORDER BY a.account_id
                """;
        return executeAccountQuery(sql, null);
    }

    public List<Account> search(String query) throws SQLException {
        // SELECT query with filters for account search by id, number, owner, type, or status.
        String sql = """
                SELECT a.account_id, a.customer_id, a.account_number, a.account_type, a.balance, a.status,
                       CONCAT(c.first_name, ' ', c.last_name) AS customer_name
                FROM accounts a
                JOIN customers c ON c.customer_id = a.customer_id
                WHERE CAST(a.account_id AS CHAR) LIKE ?
                   OR LOWER(a.account_number) LIKE ?
                   OR LOWER(a.account_type) LIKE ?
                   OR LOWER(a.status) LIKE ?
                   OR LOWER(CONCAT(c.first_name, ' ', c.last_name)) LIKE ?
                ORDER BY a.account_id
                """;
        String searchTerm = "%" + query.trim().toLowerCase() + "%";
        return executeAccountQuery(sql, searchTerm);
    }

    private List<Account> executeAccountQuery(String sql, String searchTerm) throws SQLException {
        List<Account> accounts = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            if (searchTerm != null) {
                for (int index = 1; index <= 5; index++) {
                    statement.setString(index, searchTerm);
                }
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    accounts.add(mapRow(resultSet));
                }
            }
        }
        return accounts;
    }

    public Account findById(Connection connection, int accountId) throws SQLException {
        String sql = """
                SELECT a.account_id, a.customer_id, a.account_number, a.account_type, a.balance, a.status,
                       CONCAT(c.first_name, ' ', c.last_name) AS customer_name
                FROM accounts a
                JOIN customers c ON c.customer_id = a.customer_id
                WHERE a.account_id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, accountId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRow(resultSet);
                }
            }
        }
        return null;
    }

    public void create(Account account) throws SQLException {
        // INSERT query for creating a new bank account.
        String sql = "INSERT INTO accounts (customer_id, account_number, account_type, balance, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, account.getCustomerId());
            statement.setString(2, account.getAccountNumber());
            statement.setString(3, account.getAccountType());
            statement.setBigDecimal(4, account.getBalance());
            statement.setString(5, account.getStatus());
            statement.executeUpdate();
        }
    }

    public void update(Account account) throws SQLException {
        // UPDATE query for modifying account ownership, number, type, balance, or status.
        String sql = "UPDATE accounts SET customer_id = ?, account_number = ?, account_type = ?, balance = ?, status = ? WHERE account_id = ?";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, account.getCustomerId());
            statement.setString(2, account.getAccountNumber());
            statement.setString(3, account.getAccountType());
            statement.setBigDecimal(4, account.getBalance());
            statement.setString(5, account.getStatus());
            statement.setInt(6, account.getAccountId());
            statement.executeUpdate();
        }
    }

    public void delete(int accountId) throws SQLException {
        // DELETE query for removing an account record.
        String sql = "DELETE FROM accounts WHERE account_id = ?";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, accountId);
            statement.executeUpdate();
        }
    }

    public void updateBalance(Connection connection, int accountId, BigDecimal newBalance) throws SQLException {
        // UPDATE query used inside deposit, withdrawal, and transfer transactions.
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBigDecimal(1, newBalance);
            statement.setInt(2, accountId);
            statement.executeUpdate();
        }
    }

    private Account mapRow(ResultSet resultSet) throws SQLException {
        Account account = new Account();
        account.setAccountId(resultSet.getInt("account_id"));
        account.setCustomerId(resultSet.getInt("customer_id"));
        account.setAccountNumber(resultSet.getString("account_number"));
        account.setAccountType(resultSet.getString("account_type"));
        account.setBalance(resultSet.getBigDecimal("balance"));
        account.setStatus(resultSet.getString("status"));
        account.setCustomerName(resultSet.getString("customer_name"));
        return account;
    }
}
