package com.william.dev.f1stats.data.db;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
public class SqliteConnectionFactory implements ConnectionFactory {

    public Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite::resource:f1-stats.db");
        } catch (final ClassNotFoundException | SQLException ex) {
            log.error("Error creating database connection", ex);
        }
        return connection;
    }
}
