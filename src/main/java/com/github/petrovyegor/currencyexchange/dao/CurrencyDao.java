package com.github.petrovyegor.currencyexchange.dao;

import com.github.petrovyegor.currencyexchange.exception.DBException;
import com.github.petrovyegor.currencyexchange.model.Currency;
import com.github.petrovyegor.currencyexchange.util.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

public final class CurrencyDao {
    private static final String QUERY_FAILURE_MESSAGE = "Failed to execute the query '%s', something went wrong";
    private static final String FIND_ALL_QUERY = "SELECT Id, Code, FullName, Sign FROM Currencies";
    private static final String FIND_BY_CODE = "SELECT Id, Code, FullName, Sign FROM Currencies WHERE Code = ?";
    private static final String FIND_BY_ID = "SELECT Id, Code, FullName, Sign FROM Currencies WHERE id = ?";
    private static final String INSERT_CURRENCY = "INSERT INTO Currencies (Code, FullName, Sign) VALUES (?, ?, ?)";


    public Optional<Currency> findByCode(String code) {
        try (Connection co = DataSource.getConnection();
             PreparedStatement statement = co.prepareStatement(FIND_BY_CODE)) {
            statement.setString(1, code);
            ResultSet resultSet = statement.executeQuery();

            Currency currency = null;

            if (resultSet.next()) {
                currency = new Currency(
                        resultSet.getInt("id"),
                        resultSet.getString("code"),
                        resultSet.getString("fullname"),
                        resultSet.getString("sign")
                );
            }
            return Optional.ofNullable(currency);
        } catch (SQLException e) {
            throw new DBException(SC_INTERNAL_SERVER_ERROR, QUERY_FAILURE_MESSAGE.formatted(FIND_BY_CODE));
        }
    }

    public List<Currency> findAll() {
        List<Currency> result = new ArrayList<>();
        try (Connection co = DataSource.getConnection();
             Statement statement = co.createStatement();
             ResultSet resultSet = statement.executeQuery(FIND_ALL_QUERY)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String code = resultSet.getString("code");
                String fullName = resultSet.getString("fullname");
                String sign = resultSet.getString("sign");
                result.add(new Currency(id, code, fullName, sign));
            }
            return result;
        } catch (SQLException e) {
            throw new DBException(SC_INTERNAL_SERVER_ERROR, QUERY_FAILURE_MESSAGE.formatted(FIND_ALL_QUERY));
        }
    }

    public Currency save(Currency currency) {
        try (Connection co = DataSource.getConnection();
             PreparedStatement statement = co.prepareStatement(INSERT_CURRENCY)) {
            statement.setString(1, currency.getCode());
            statement.setString(2, currency.getFullName());
            statement.setString(3, currency.getSign());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();

            currency.setId(resultSet.getInt(1));
            return currency;
        } catch (SQLException e) {
            throw new DBException(SC_INTERNAL_SERVER_ERROR, QUERY_FAILURE_MESSAGE.formatted(FIND_ALL_QUERY));
        }
    }

    public Optional<Currency> findById(int id) {
        try (Connection co = DataSource.getConnection();
             PreparedStatement statement = co.prepareStatement(FIND_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            Currency currency = null;

            if (resultSet.next()) {
                currency = new Currency(
                        resultSet.getInt("id"),
                        resultSet.getString("code"),
                        resultSet.getString("fullname"),
                        resultSet.getString("sign")
                );
            }
            return Optional.ofNullable(currency);
        } catch (SQLException e) {
            throw new DBException(SC_INTERNAL_SERVER_ERROR, QUERY_FAILURE_MESSAGE.formatted(FIND_BY_ID));
        }
    }
}

