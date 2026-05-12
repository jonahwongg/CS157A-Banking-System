package com.bank.dao;

import com.bank.model.Customer;
import com.bank.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDao {
    public List<Customer> findAll() throws SQLException {
        // SELECT query used to display all customer records on the dashboard.
        String sql = "SELECT customer_id, first_name, last_name, email, phone_number FROM customers ORDER BY customer_id";
        return executeCustomerQuery(sql, null);
    }

    public List<Customer> search(String query) throws SQLException {
        // SELECT query with LIKE filters so users can search customers by several fields.
        String sql = """
                SELECT customer_id, first_name, last_name, email, phone_number
                FROM customers
                WHERE CAST(customer_id AS CHAR) LIKE ?
                   OR LOWER(first_name) LIKE ?
                   OR LOWER(last_name) LIKE ?
                   OR LOWER(email) LIKE ?
                   OR LOWER(phone_number) LIKE ?
                ORDER BY customer_id
                """;
        String searchTerm = "%" + query.trim().toLowerCase() + "%";
        return executeCustomerQuery(sql, searchTerm);
    }

    private List<Customer> executeCustomerQuery(String sql, String searchTerm) throws SQLException {
        List<Customer> customers = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            if (searchTerm != null) {
                for (int index = 1; index <= 5; index++) {
                    statement.setString(index, searchTerm);
                }
            }

            try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                customers.add(mapRow(resultSet));
            }
            }
        }

        return customers;
    }

    public void create(Customer customer) throws SQLException {
        // INSERT query for adding a new customer record.
        String sql = "INSERT INTO customers (first_name, last_name, email, phone_number) VALUES (?, ?, ?, ?)";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, customer.getFirstName());
            statement.setString(2, customer.getLastName());
            statement.setString(3, customer.getEmail());
            statement.setString(4, customer.getPhoneNumber());
            statement.executeUpdate();
        }
    }

    public void update(Customer customer) throws SQLException {
        // UPDATE query for editing an existing customer.
        String sql = "UPDATE customers SET first_name = ?, last_name = ?, email = ?, phone_number = ? WHERE customer_id = ?";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, customer.getFirstName());
            statement.setString(2, customer.getLastName());
            statement.setString(3, customer.getEmail());
            statement.setString(4, customer.getPhoneNumber());
            statement.setInt(5, customer.getCustomerId());
            statement.executeUpdate();
        }
    }

    public void delete(int customerId) throws SQLException {
        // DELETE query for removing a customer by primary key.
        String sql = "DELETE FROM customers WHERE customer_id = ?";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerId);
            statement.executeUpdate();
        }
    }

    private Customer mapRow(ResultSet resultSet) throws SQLException {
        Customer customer = new Customer();
        customer.setCustomerId(resultSet.getInt("customer_id"));
        customer.setFirstName(resultSet.getString("first_name"));
        customer.setLastName(resultSet.getString("last_name"));
        customer.setEmail(resultSet.getString("email"));
        customer.setPhoneNumber(resultSet.getString("phone_number"));
        return customer;
    }
}
