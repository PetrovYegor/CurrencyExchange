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
            if (rs.next()) {
                //while (rs.next()) {
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

    public ExchangeRate save(int baseCurrencyId, int targetCurrencyId, double rate) throws SQLException {
        String query = "INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) VALUES (?, ?, ?)";
        try (Connection co = DatabaseManager.getConnection(); PreparedStatement statement = co.prepareStatement(query)) {
            statement.setInt(1, baseCurrencyId);
            statement.setInt(2, targetCurrencyId);
            statement.setDouble(3, rate);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();

            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                return new ExchangeRate(id, baseCurrencyId, targetCurrencyId, rate);
            } else {
                throw new SQLException("Failed to get generated ID");
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateRate(double newRate, int exchangeRateId) throws SQLException {
        String query = "UPDATE ExchangeRates SET Rate = ? WHERE Id = ?;";
        try (Connection co = DatabaseManager.getConnection();
             PreparedStatement statement = co.prepareStatement(query)) {
            statement.setDouble(1, newRate);
            statement.setInt(2, exchangeRateId);
            statement.executeUpdate();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
