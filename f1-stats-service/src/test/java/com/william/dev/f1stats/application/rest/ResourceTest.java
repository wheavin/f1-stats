package com.william.dev.f1stats.application.rest;

import com.william.dev.f1stats.data.db.SqliteConnectionFactory;
import de.hilling.junit.cdi.CdiTestJunitExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

@ExtendWith(CdiTestJunitExtension.class)
@ExtendWith(MockitoExtension.class)
public class ResourceTest {
    @Mock
    protected Connection mockConnection;

    @Mock
    protected PreparedStatement mockStatement;

    @Mock
    protected SqliteConnectionFactory mockConnectionFactory;

    @BeforeEach
    public void setup() throws Exception {
        lenient().doReturn(mockStatement).when(mockConnection).prepareStatement(anyString());
        lenient().doReturn(mockConnection).when(mockConnectionFactory).getConnection();
    }
}
