package com.github.petrovyegor.currencyexchange.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {
    //private static HikariConfig config;
    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource dataSource;
    private final static String DATABASE_URL = "jdbc:sqlite::resource:CurrencyExchange.sqlite";

    static {
        //config = new HikariConfig();
        config.setDriverClassName("org.sqlite.JDBC");
        config.setJdbcUrl("jdbc:sqlite:C:\\roadmap\\CurrencyExchange\\src\\main\\resources\\CurrencyExchange.sqlite");
        //config.setJdbcUrl(DATABASE_URL);

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        config.setConnectionTimeout(30000);
        dataSource = new HikariDataSource(config);
    }

    private DataSource() {
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
