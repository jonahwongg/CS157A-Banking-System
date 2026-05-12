package com.bank.service;

import com.bank.dao.UserDao;
import com.bank.model.User;
import com.bank.util.PasswordUtil;

import java.sql.SQLException;

public class AuthService {
    private final UserDao userDao = new UserDao();

    public User authenticate(String username, String password) throws SQLException {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return null;
        }

        User user = userDao.findByUsername(username.trim());
        if (user == null) {
            return null;
        }

        String providedHash = PasswordUtil.hash(password);
        if (!providedHash.equals(user.getPasswordHash())) {
            return null;
        }

        return user;
    }
}
