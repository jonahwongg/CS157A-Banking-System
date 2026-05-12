package com.bank.dao;

import com.bank.model.Loan;
import com.bank.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoanDao {
    public List<Loan> findAll() throws SQLException {
        // SELECT query used to display all loan records with customer names.
        String sql = """
                SELECT l.loan_id, l.customer_id, l.principal_amount, l.interest_rate, l.term_months, l.status,
                       l.application_date, CONCAT(c.first_name, ' ', c.last_name) AS customer_name
                FROM loans l
                JOIN customers c ON c.customer_id = l.customer_id
                ORDER BY l.loan_id
                """;
        List<Loan> loans = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                loans.add(mapRow(resultSet));
            }
        }

        return loans;
    }

    public void create(Loan loan) throws SQLException {
        // INSERT query for creating a new loan application.
        String sql = """
                INSERT INTO loans (customer_id, principal_amount, interest_rate, term_months, status, application_date)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, loan.getCustomerId());
            statement.setBigDecimal(2, loan.getPrincipalAmount());
            statement.setBigDecimal(3, loan.getInterestRate());
            statement.setInt(4, loan.getTermMonths());
            statement.setString(5, loan.getStatus());
            statement.setDate(6, loan.getApplicationDate());
            statement.executeUpdate();
        }
    }

    public void update(Loan loan) throws SQLException {
        // UPDATE query for loan processing and general loan edits.
        String sql = """
                UPDATE loans
                SET customer_id = ?, principal_amount = ?, interest_rate = ?, term_months = ?, status = ?, application_date = ?
                WHERE loan_id = ?
                """;
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, loan.getCustomerId());
            statement.setBigDecimal(2, loan.getPrincipalAmount());
            statement.setBigDecimal(3, loan.getInterestRate());
            statement.setInt(4, loan.getTermMonths());
            statement.setString(5, loan.getStatus());
            statement.setDate(6, loan.getApplicationDate());
            statement.setInt(7, loan.getLoanId());
            statement.executeUpdate();
        }
    }

    public void delete(int loanId) throws SQLException {
        // DELETE query for removing a loan record.
        String sql = "DELETE FROM loans WHERE loan_id = ?";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, loanId);
            statement.executeUpdate();
        }
    }

    private Loan mapRow(ResultSet resultSet) throws SQLException {
        Loan loan = new Loan();
        loan.setLoanId(resultSet.getInt("loan_id"));
        loan.setCustomerId(resultSet.getInt("customer_id"));
        loan.setPrincipalAmount(resultSet.getBigDecimal("principal_amount"));
        loan.setInterestRate(resultSet.getBigDecimal("interest_rate"));
        loan.setTermMonths(resultSet.getInt("term_months"));
        loan.setStatus(resultSet.getString("status"));
        loan.setApplicationDate(resultSet.getDate("application_date"));
        loan.setCustomerName(resultSet.getString("customer_name"));
        return loan;
    }
}
