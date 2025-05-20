package com.github.petrovyegor.currencyexchange.dao;

import com.github.petrovyegor.currencyexchange.model.Currency;
import com.github.petrovyegor.currencyexchange.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CurrencyDao {
    public Currency getByCode(String code) throws SQLException, ClassNotFoundException {
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
        }
    }

    public List<Currency> getAll() throws SQLException, ClassNotFoundException {
        List<Currency> result = new ArrayList<>();
        String query = "SELECT Id, Code, FullName, Sign FROM Currencies";
        try (Connection co = DatabaseManager.getConnection();
             Statement statement = co.createStatement();
            ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String code = resultSet.getString("code");
                String fullName = resultSet.getString("fullname");
                String sign = resultSet.getString("sign");
                result.add(new Currency(id, code, fullName, sign));
            }
            return result;
        }     }

    public Currency save(Currency source) throws SQLException {
        String query = "INSERT INTO Currencies (Code, FullName, Sign) VALUES (?, ?, ?);";
        try (Connection co = DatabaseManager.getConnection(); PreparedStatement statement = co.prepareStatement(query)) {
            statement.setString(1, source.getCode());
            statement.setString(2, source.getFullName());
            statement.setString(3, source.getSign());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();

            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                source.setId(id);
                return source;
            } else {
                throw new SQLException("Failed to get generated ID");
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Currency getById(int id) throws SQLException {
        Currency result = null;
        String query = "SELECT Id, Code, FullName, Sign FROM Currencies WHERE id = ?";
        try (Connection co = DatabaseManager.getConnection();
             PreparedStatement statement = co.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String code = rs.getString("code");
                String name = rs.getString("fullname");
                String sign = rs.getString("sign");
                rs.close();//везде проверить, что закрываю
                result = new Currency(id, code, name, sign);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (result != null) {
            return result;
        } else {
            throw new RuntimeException();
        }
    }
}
