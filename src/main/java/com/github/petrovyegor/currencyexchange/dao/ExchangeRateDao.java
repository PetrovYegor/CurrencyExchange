package com.github.petrovyegor.currencyexchange.dao;

import com.github.petrovyegor.currencyexchange.exception.DBException;
import com.github.petrovyegor.currencyexchange.model.ExchangeRate;
import com.github.petrovyegor.currencyexchange.util.DataSource;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

public final class ExchangeRateDao {
    private static final String FIND_ALL_QUERY = "SELECT id, basecurrencyid, targetcurrencyid, rate FROM ExchangeRates";
    private static final String FIND_BY_CURRENCY_CODES = """
            SELECT er.id, er.basecurrencyid, er.targetcurrencyid, er.rate
            FROM ExchangeRates er
            JOIN Currencies base_cur ON er.BaseCurrencyId = base_cur.Id
            JOIN Currencies target_cur ON er.TargetCurrencyId = target_cur."Id"
            WHERE base_cur.Code = ? AND target_cur.Code = ?
            """;
    private static final String INSERT_EXCHANGE_RATE = "INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) VALUES (?, ?, ?)";
    private static final String UPDATE_EXCHANGE_RATE = "UPDATE ExchangeRates SET Rate = ? WHERE Id = ?";

    public List<ExchangeRate> findAll() {
        List<ExchangeRate> result = new ArrayList<>();
        try (Connection co = DataSource.getConnection();
             Statement statement = co.createStatement();
             ResultSet resultSet = statement.executeQuery(FIND_ALL_QUERY)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int baseCurrencyId = resultSet.getInt("basecurrencyid");
                int targetCurrencyId = resultSet.getInt("targetcurrencyid");
                BigDecimal rate = new BigDecimal("rate");
                result.add(new ExchangeRate(id, baseCurrencyId, targetCurrencyId, rate));
            }
            return result;
        } catch (SQLException e) {
            throw new DBException(SC_INTERNAL_SERVER_ERROR, String.format("Failed to get all exchange rates"));
        }
    }

    public Optional<ExchangeRate> findByCurrencyCodes(String baseCode, String targetCode) {
        try (Connection co = DataSource.getConnection();
             PreparedStatement statement = co.prepareStatement(FIND_BY_CURRENCY_CODES)) {
            statement.setString(1, baseCode);
            statement.setString(2, targetCode);
            ResultSet resultSet = statement.executeQuery();

            ExchangeRate exchangeRate = null;

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                int baseCurrencyId = resultSet.getInt("basecurrencyid");
                int targetCurrencyId = resultSet.getInt("targetcurrencyid");
                BigDecimal rate = new BigDecimal("rate");
                exchangeRate = new ExchangeRate(id, baseCurrencyId, targetCurrencyId, rate);
            }
            return Optional.ofNullable(exchangeRate);
        } catch (SQLException e) {
            throw new DBException(SC_INTERNAL_SERVER_ERROR, String.format("Failed to get exchange rate by base currency code '%s' and target currency code '%s'", baseCode, targetCode));
        }
    }

    public ExchangeRate save(ExchangeRate exchangeRate) {
        try (Connection co = DataSource.getConnection();
             PreparedStatement statement = co.prepareStatement(INSERT_EXCHANGE_RATE)) {
            statement.setInt(1, exchangeRate.getBaseCurrencyId());
            statement.setInt(2, exchangeRate.getTargetCurrencyId());
            statement.setBigDecimal(3, exchangeRate.getRate());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();

            exchangeRate.setId(resultSet.getInt(1));
            return exchangeRate;
        } catch (SQLException e) {
            throw new DBException(SC_INTERNAL_SERVER_ERROR, String.format("Failed to save exchange rate with base currency id '%s', target currency id '%s' and rate '%s'", exchangeRate.getBaseCurrencyId(), exchangeRate.getTargetCurrencyId(), exchangeRate.getRate()));
        }
    }

    public ExchangeRate updateRate(ExchangeRate exchangeRate) {
        try (Connection co = DataSource.getConnection();
             PreparedStatement statement = co.prepareStatement(UPDATE_EXCHANGE_RATE)) {
            statement.setBigDecimal(1, exchangeRate.getRate());
            statement.setInt(2, exchangeRate.getId());
            statement.executeUpdate();
            return exchangeRate;
        } catch (SQLException e) {
            throw new DBException(SC_INTERNAL_SERVER_ERROR, String.format("Failed to update exchange rate with id '%s' and new rate value '%s'", exchangeRate.getId(), exchangeRate.getRate()));
        }
    }
}
