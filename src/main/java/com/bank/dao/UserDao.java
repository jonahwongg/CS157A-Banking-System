package com.bank.dao;

import com.bank.model.User;
import com.bank.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    public User findByUsername(String username) throws SQLException {
        // SELECT query for login so the application can compare the submitted password hash.
        String sql = "SELECT user_id, username, password_hash, full_name, role_name FROM users WHERE username = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setUserId(resultSet.getInt("user_id"));
                    user.setUsername(resultSet.getString("username"));
                    user.setPasswordHash(resultSet.getString("password_hash"));
                    user.setFullName(resultSet.getString("full_name"));
                    user.setRoleName(resultSet.getString("role_name"));
                    return user;
                }
            }
        }

        return null;
    }
}
