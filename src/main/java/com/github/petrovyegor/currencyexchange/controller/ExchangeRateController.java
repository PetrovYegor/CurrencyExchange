package com.github.petrovyegor.currencyexchange.controller;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.petrovyegor.currencyexchange.model.ExchangeRate;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRateController extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            List<ExchangeRate> currencies = getAllExchangeRates();

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // Преобразуем список валют в JSON и пишем в ответ
            objectMapper.writeValue(response.getWriter(), currencies);

        } catch (ClassNotFoundException e) {
            // Обработка ошибок
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Database driver not found\"}");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Database error: " + e.getMessage() + "\"}");
        } catch (DatabindException e) {
            throw new RuntimeException(e);
        } catch (StreamWriteException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private List<ExchangeRate> getAllExchangeRates() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        Connection co = DriverManager.getConnection("jdbc:sqlite:C:\\roadmap\\CurrencyExchange\\CurrencyExchange.sqlite");
        try (co; Statement statement = co.createStatement();) {
            String query = "SELECT id, basecurrencyid, targetcurrencyid, rate FROM ExchangeRates;";
            ResultSet rs = statement.executeQuery(query);

            List<ExchangeRate> exchangeRates = new ArrayList<>();

            while (rs.next()) {
                int id = rs.getInt(1);//можно указать номер столбца, либо полное название
                int baseCurrencyId = rs.getInt(2);
                int targetCurrencyId = rs.getInt(3);
                double rate = rs.getDouble(4);
                exchangeRates.add(new ExchangeRate(id, baseCurrencyId, targetCurrencyId, rate));
            }
            rs.close();
            return exchangeRates;
        }

    }

}
