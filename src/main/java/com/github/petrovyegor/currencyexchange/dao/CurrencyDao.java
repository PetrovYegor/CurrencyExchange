package com.github.petrovyegor.currencyexchange.dao;

import com.github.petrovyegor.currencyexchange.model.Currency;
import com.github.petrovyegor.currencyexchange.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CurrencyDao {
    public Currency getByCode(String code) throws SQLException {
        try (Connection co = DatabaseManager.getConnection();
             PreparedStatement statement = co.prepareStatement("SELECT Id, Code, FullName, Sign FROM Currencies WHERE Code = ?")) {
            statement.setString(1, code.toUpperCase());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Currency(
                        resultSet.getInt("id"),
                        resultSet.getString("code"),
                        resultSet.getString("fullname"),
                        resultSet.getString("sign")
                );
            } else {
                return null;
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Currency> getAll() throws SQLException {
        List<Currency> result = new ArrayList<>();
        try (Connection co = DatabaseManager.getConnection();
             Statement statement = co.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT Id, Code, FullName, Sign FROM Currencies");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String code = resultSet.getString("code");
                String fullName = resultSet.getString("fullname");
                String sign = resultSet.getString("sign");
                result.add(new Currency(id, code, fullName, sign));
            }
            return result;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Currency save(String code, String name, String sign) throws SQLException {
        String query = "INSERT INTO Currencies (Code, FullName, Sign) VALUES (?, ?, ?);";
        try (Connection co = DatabaseManager.getConnection(); PreparedStatement statement = co.prepareStatement(query)) {
            statement.setString(1, code.toUpperCase());
            statement.setString(2, name);
            statement.setString(3, sign);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();

            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                return new Currency(id, code, name, sign);
            } else {
                throw new SQLException("Failed to get generated ID");
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
