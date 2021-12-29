package com.william.dev.f1stats.data.db;

public final class SqlStatements {
    public static final String GET_ALL_CIRCUITS_QUERY = "SELECT * FROM Circuits";
    public static final String GET_ALL_CIRCUIT_NAMES_QUERY = "SELECT name FROM Circuits";
    public static final String GET_CIRCUIT_BY_NAME_QUERY = "SELECT * FROM Circuits WHERE name LIKE ?";
    public static final String INSERT_CIRCUIT_SQL = "INSERT INTO Circuits (name, country, wiki) VALUES (?, ?, ?)";

    public static final String GET_ALL_DRIVERS_QUERY = "SELECT * FROM Drivers";
    public static final String GET_DRIVER_BY_NAME_QUERY = "SELECT * FROM Drivers WHERE firstName LIKE ? AND lastName like ?";
    public static final String INSERT_DRIVER_SQL = "INSERT INTO Drivers (firstName, lastName, nationality, dateOfBirth, wiki) VALUES (?, ?, ?, ?, ?)";
}
