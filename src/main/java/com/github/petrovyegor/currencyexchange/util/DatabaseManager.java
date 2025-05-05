package com.github.petrovyegor.currencyexchange.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DatabaseManager {
    private static final String DB_URL;
    private DatabaseManager() {
    }

    //сначала тренировка работать с файлом application.properties
    //затем, когда потребуется деплой, переключиться на использование переменных окружения
    static {
        try (InputStream inputStream = DatabaseManager.class.getClassLoader().getResourceAsStream("application.properties");) {
            if (inputStream == null) {
                throw new RuntimeException("Config file not found!");
            }
            Properties properties = new Properties();
            properties.load(inputStream);
            DB_URL = properties.getProperty("db.url");


        } catch (Exception e) {//слишком широкий уровень ошибок
            throw new RuntimeException("Failed to load config", e);
        }
    }

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        return DriverManager.getConnection(DB_URL);
    }
}
