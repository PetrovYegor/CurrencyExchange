package com.github.petrovyegor.currencyexchange.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseManager {
    private DatabaseManager(){}

    private static final String DB_URL = "jdbc:sqlite:C:\\roadmap\\CurrencyExchange\\src\\main\\resources\\CurrencyExchange.sqlite";

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        return DriverManager.getConnection(DB_URL);
    }
}
