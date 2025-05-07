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
            String query = "SELECT id, basecurrencyid, targetcurrencyid, rate FROM ExchangeRates;";
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
}
