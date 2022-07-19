package com.william.dev.f1stats.data.db;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DatabaseClientTestBase {
    @Mock
    protected ConnectionFactory mockConnectionFactory;

    @Mock
    protected Connection mockConnection;

    @Mock
    protected PreparedStatement mockStatement;

    @BeforeEach
    public void mockDbConnection() throws SQLException {
        doReturn(mockConnection).when(mockConnectionFactory).getConnection();
        doReturn(mockStatement).when(mockConnection).prepareStatement(anyString());
        lenient().when(mockStatement.executeBatch()).thenReturn(new int[]{});
    }

    protected void mockSqlExceptionOnQuery() throws SQLException {
        when(mockStatement.executeQuery()).thenThrow(new SQLException("Some error occurred"));
    }

    protected void mockSqlExceptionOnAdd() throws SQLException {
        when(mockStatement.executeBatch()).thenThrow(new SQLException("Some error occurred"));
    }

    protected void assertExecuteBatch(final int itemCount) throws SQLException {
        verify(mockStatement, times(itemCount)).addBatch();
        verify(mockStatement).executeBatch();
    }
}
