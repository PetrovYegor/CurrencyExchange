package com.github.petrovyegor.currencyexchange.dao;

import com.github.petrovyegor.currencyexchange.exception.DBException;
import com.github.petrovyegor.currencyexchange.model.ExchangeRate;
import com.github.petrovyegor.currencyexchange.util.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

public final class ExchangeRateDao {
    private static final String QUERY_FAILURE_MESSAGE = "Failed to execute the query '%s', something went wrong";
    private static final String FIND_ALL_QUERY = "SELECT id, basecurrencyid, targetcurrencyid, rate FROM ExchangeRates";
    private static final String FIND_BY_CURRENCY_CODES = "SELECT id, basecurrencyid, targetcurrencyid, rate FROM ExchangeRates WHERE basecurrencyid = ? AND targetcurrencyid = ?";

    public List<ExchangeRate> findAll() {
        List<ExchangeRate> result = new ArrayList<>();
        try (Connection co = DataSource.getConnection();
             Statement statement = co.createStatement();
             ResultSet resultSet = statement.executeQuery(FIND_ALL_QUERY)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int baseCurrencyId = resultSet.getInt("basecurrencyid");
                int targetCurrencyId = resultSet.getInt("targetcurrencyid");
                double rate = resultSet.getDouble("rate");
                result.add(new ExchangeRate(id, baseCurrencyId, targetCurrencyId, rate));
            }
            return result;
        } catch (SQLException e) {
            throw new DBException(SC_INTERNAL_SERVER_ERROR, QUERY_FAILURE_MESSAGE.formatted(FIND_ALL_QUERY));
        }
    }

    public Optional<ExchangeRate> findByCurrencyIds(int baseCurrencyId, int targetCurrencyId) {
        try (Connection co = DataSource.getConnection();
             PreparedStatement statement = co.prepareStatement(FIND_BY_CURRENCY_CODES)) {
            statement.setInt(1, baseCurrencyId);
            statement.setInt(2, targetCurrencyId);
            ResultSet resultSet = statement.executeQuery();

            ExchangeRate exchangeRate = null;

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                double rate = resultSet.getDouble("rate");
                exchangeRate = new ExchangeRate(id, baseCurrencyId, targetCurrencyId, rate);
            }
            return Optional.ofNullable(exchangeRate);
        } catch (SQLException e) {
            throw new DBException(SC_INTERNAL_SERVER_ERROR, QUERY_FAILURE_MESSAGE.formatted(FIND_BY_CURRENCY_CODES));
        }
    }
//
//    public ExchangeRate save(int baseCurrencyId, int targetCurrencyId, double rate) throws SQLException {
//        String query = "INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) VALUES (?, ?, ?)";
//        ResultSet resultSet = null;
//        try (Connection co = DatabaseManager.getConnection(); PreparedStatement statement = co.prepareStatement(query)) {
//            statement.setInt(1, baseCurrencyId);
//            statement.setInt(2, targetCurrencyId);
//            statement.setDouble(3, rate);
//            statement.executeUpdate();
//            resultSet = statement.getGeneratedKeys();
//
//            if (resultSet.next()) {
//                int id = resultSet.getInt(1);
//                return new ExchangeRate(id, baseCurrencyId, targetCurrencyId, rate);
//            } else {
//                throw new SQLException("Failed to get generated ID");
//            }
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        } finally {
//            if (resultSet != null) {
//                resultSet.close();
//            }
//        }
//    }
//
//    public void updateRate(double newRate, int exchangeRateId) throws SQLException {
//        String query = "UPDATE ExchangeRates SET Rate = ? WHERE Id = ?;";
//        try (Connection co = DatabaseManager.getConnection();
//             PreparedStatement statement = co.prepareStatement(query)) {
//            statement.setDouble(1, newRate);
//            statement.setInt(2, exchangeRateId);
//            statement.executeUpdate();
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
