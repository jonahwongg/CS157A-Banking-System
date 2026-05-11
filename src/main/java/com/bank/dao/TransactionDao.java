package com.bank.dao;

import com.bank.model.BankTransaction;
import com.bank.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class TransactionDao {
    public List<BankTransaction> findAll() throws SQLException {
        String sql = """
                SELECT t.transaction_id, t.from_account_id, t.to_account_id, t.transaction_type, t.amount,
                       t.description, t.transaction_time,
                       fa.account_number AS from_account_number,
                       ta.account_number AS to_account_number
                FROM transactions t
                LEFT JOIN accounts fa ON fa.account_id = t.from_account_id
                LEFT JOIN accounts ta ON ta.account_id = t.to_account_id
                ORDER BY t.transaction_time DESC, t.transaction_id DESC
                """;
        return executeTransactionQuery(sql, null);
    }

    public List<BankTransaction> search(String query) throws SQLException {
        String sql = """
                SELECT t.transaction_id, t.from_account_id, t.to_account_id, t.transaction_type, t.amount,
                       t.description, t.transaction_time,
                       fa.account_number AS from_account_number,
                       ta.account_number AS to_account_number
                FROM transactions t
                LEFT JOIN accounts fa ON fa.account_id = t.from_account_id
                LEFT JOIN accounts ta ON ta.account_id = t.to_account_id
                WHERE CAST(t.transaction_id AS CHAR) LIKE ?
                   OR LOWER(t.transaction_type) LIKE ?
                   OR LOWER(t.description) LIKE ?
                   OR LOWER(COALESCE(fa.account_number, '')) LIKE ?
                   OR LOWER(COALESCE(ta.account_number, '')) LIKE ?
                   OR CAST(t.amount AS CHAR) LIKE ?
                ORDER BY t.transaction_time DESC, t.transaction_id DESC
                """;
        String searchTerm = "%" + query.trim().toLowerCase() + "%";
        return executeTransactionQuery(sql, searchTerm);
    }

    private List<BankTransaction> executeTransactionQuery(String sql, String searchTerm) throws SQLException {
        List<BankTransaction> transactions = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            if (searchTerm != null) {
                for (int index = 1; index <= 6; index++) {
                    statement.setString(index, searchTerm);
                }
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    transactions.add(mapRow(resultSet));
                }
            }
        }
        return transactions;
    }

    public void create(Connection connection, BankTransaction transaction) throws SQLException {
        String sql = """
                INSERT INTO transactions (from_account_id, to_account_id, transaction_type, amount, description)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            if (transaction.getFromAccountId() == null) {
                statement.setNull(1, Types.INTEGER);
            } else {
                statement.setInt(1, transaction.getFromAccountId());
            }
            if (transaction.getToAccountId() == null) {
                statement.setNull(2, Types.INTEGER);
            } else {
                statement.setInt(2, transaction.getToAccountId());
            }
            statement.setString(3, transaction.getTransactionType());
            statement.setBigDecimal(4, transaction.getAmount());
            statement.setString(5, transaction.getDescription());
            statement.executeUpdate();
        }
    }

    public void delete(int transactionId) throws SQLException {
        String sql = "DELETE FROM transactions WHERE transaction_id = ?";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, transactionId);
            statement.executeUpdate();
        }
    }

    private BankTransaction mapRow(ResultSet resultSet) throws SQLException {
        BankTransaction transaction = new BankTransaction();
        transaction.setTransactionId(resultSet.getInt("transaction_id"));
        transaction.setFromAccountId((Integer) resultSet.getObject("from_account_id"));
        transaction.setToAccountId((Integer) resultSet.getObject("to_account_id"));
        transaction.setTransactionType(resultSet.getString("transaction_type"));
        transaction.setAmount(resultSet.getBigDecimal("amount"));
        transaction.setDescription(resultSet.getString("description"));
        transaction.setTransactionTime(resultSet.getTimestamp("transaction_time"));
        transaction.setFromAccountNumber(resultSet.getString("from_account_number"));
        transaction.setToAccountNumber(resultSet.getString("to_account_number"));
        return transaction;
    }
}
