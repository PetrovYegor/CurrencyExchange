package com.github.petrovyegor.currencyexchange.dao;

import com.github.petrovyegor.currencyexchange.model.ExchangeRate;
import com.github.petrovyegor.currencyexchange.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateDao {
    public List<ExchangeRate> getAll() throws SQLException {
        List<ExchangeRate> result = new ArrayList<>();
        try (Connection co = DatabaseManager.getConnection();
             Statement statement = co.createStatement()) {
            String query = "SELECT id, basecurrencyid, targetcurrencyid, rate FROM ExchangeRates";
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt("id");
                int baseCurrencyId = rs.getInt("basecurrencyid");
                int targetCurrencyId = rs.getInt("targetcurrencyid");
                double rate = rs.getDouble("rate");
                result.add(new ExchangeRate(id, baseCurrencyId, targetCurrencyId, rate));
            }
            rs.close();
            return result;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public ExchangeRate getByCurrenciesIds(int baseCurrencyId, int targetCurrencyId) throws SQLException {
        ExchangeRate result = null;
        String query = "SELECT id, basecurrencyid, targetcurrencyid, rate FROM ExchangeRates WHERE basecurrencyid = ? AND targetcurrencyid = ?";
        try (Connection co = DatabaseManager.getConnection();
             PreparedStatement statement = co.prepareStatement(query)) {
            statement.setInt(1, baseCurrencyId);
            statement.setInt(2, targetCurrencyId);
            ResultSet rs = statement.executeQuery();
            //if (rs.next()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                int resultBaseCurrencyId = rs.getInt("basecurrencyid");
                int resultTargetCurrencyId = rs.getInt("targetcurrencyid");
                double rate = rs.getDouble("rate");
                rs.close();//везде проверить, что закрываю
                result = new ExchangeRate(id, resultBaseCurrencyId, resultTargetCurrencyId, rate);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
//    public ExchangeRateResponseDTO save(String baseCurrencyCode, String targetCurrencyCode, double rate) throws SQLException {
//        String query = "INSERT INTO Currencies (Code, FullName, Sign) VALUES (?, ?, ?);";
//        try (Connection co = DatabaseManager.getConnection(); PreparedStatement statement = co.prepareStatement(query)) {
//            statement.setString(1, code.toUpperCase());
//            statement.setString(2, name);
//            statement.setString(3, sign);
//            statement.executeUpdate();
//            ResultSet resultSet = statement.getGeneratedKeys();
//
//            if (resultSet.next()) {
//                int id = resultSet.getInt(1);
//                return new Currency(id, code, name, sign);
//            } else {
//                throw new SQLException("Failed to get generated ID");
//            }
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
