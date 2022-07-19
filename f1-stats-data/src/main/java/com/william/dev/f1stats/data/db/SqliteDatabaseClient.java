package com.william.dev.f1stats.data.db;

import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Slf4j
public class SqliteDatabaseClient {
    @Inject
    private ConnectionFactory connectionFactory;

    protected Connection getConnection() {
        return connectionFactory.getConnection();
    }

    protected PreparedStatement prepareStatement(final Connection connection, final String sqlQuery,
                                                 final String... params) throws SQLException {
        final PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        for (int paramIndex = 1; paramIndex <= params.length; paramIndex++) {
            preparedStatement.setString(paramIndex, params[paramIndex - 1]);
        }
        return preparedStatement;
    }
}
