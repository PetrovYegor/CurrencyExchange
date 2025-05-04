package com.github.petrovyegor.currencyexchange.dao;

import com.github.petrovyegor.currencyexchange.model.Currency;
import com.github.petrovyegor.currencyexchange.util.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CurrencyDao {
    public Currency findByCode(String code) throws SQLException {
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

    public List<Currency> getAll() {
        return null;
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
