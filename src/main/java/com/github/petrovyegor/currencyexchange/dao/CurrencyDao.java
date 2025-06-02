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

//    public static void main(String[] args) throws SQLException, ClassNotFoundException {
//        HashMap<String, String> test = new HashMap<>();
//        test.put("message", "12123123");
//        System.out.println(test);
//        int a = 123;
//    }
//
//    public Currency save(Currency source) throws SQLException {
//        String query = "INSERT INTO Currencies (Code, FullName, Sign) VALUES (?, ?, ?);";
//        try (Connection co = DatabaseManager.getConnection(); PreparedStatement statement = co.prepareStatement(query)) {
//            statement.setString(1, source.getCode());
//            statement.setString(2, source.getFullName());
//            statement.setString(3, source.getSign());
//            statement.executeUpdate();
//            ResultSet resultSet = statement.getGeneratedKeys();
//
//            if (resultSet.next()) {
//                int id = resultSet.getInt(1);
//                source.setId(id);
//                return source;
//            } else {
//                throw new SQLException("Failed to get generated ID");
//            }
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public Currency getById(int id) throws SQLException {
//        Currency result = null;
//        ResultSet resultSet = null;
//        String query = "SELECT Id, Code, FullName, Sign FROM Currencies WHERE id = ?";
//        try (Connection co = DatabaseManager.getConnection();
//             PreparedStatement statement = co.prepareStatement(query)) {
//            statement.setInt(1, id);
//            resultSet = statement.executeQuery();
//            if (resultSet.next()) {
//                String code = resultSet.getString("code");
//                String name = resultSet.getString("fullname");
//                String sign = resultSet.getString("sign");
//                result = new Currency(id, code, name, sign);
//            }
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        } finally {
//            resultSet.close();
//            if (result != null) {
//                return result;
//            } else {
//                throw new RuntimeException();
//            }
//        }
//    }
}
