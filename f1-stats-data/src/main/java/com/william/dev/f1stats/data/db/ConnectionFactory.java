package com.william.dev.f1stats.data.db;

import java.sql.Connection;

public interface ConnectionFactory {

    Connection getConnection();
}
