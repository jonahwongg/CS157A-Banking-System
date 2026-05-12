package com.bank.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class DatabaseConfig {
    private static final Properties PROPERTIES = new Properties();

    static {
        // Load JDBC settings once when the application starts so every DAO can reuse them.
        try (InputStream inputStream = DatabaseConfig.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (inputStream == null) {
                throw new IllegalStateException("db.properties file was not found.");
            }
            PROPERTIES.load(inputStream);
            // Register the JDBC driver class before any connection requests are made.
            Class.forName(PROPERTIES.getProperty("db.driver"));
        } catch (IOException | ClassNotFoundException ex) {
            // Fail fast if the application cannot reach its database configuration.
            throw new ExceptionInInitializerError(ex);
        }
    }

    private DatabaseConfig() {
    }

    public static String getUrl() {
        return PROPERTIES.getProperty("db.url");
    }

    public static String getUsername() {
        return PROPERTIES.getProperty("db.username");
    }

    public static String getPassword() {
        return PROPERTIES.getProperty("db.password");
    }
}
