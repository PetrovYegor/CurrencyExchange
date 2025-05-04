package com.github.petrovyegor.currencyexchange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.petrovyegor.currencyexchange.model.Currency;
import com.github.petrovyegor.currencyexchange.service.CurrencyService;
import com.github.petrovyegor.currencyexchange.util.DatabaseManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CurrencyController extends HttpServlet {
    private final CurrencyService currencyService = new CurrencyService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        if (path == null) {
            try {
                getAllCurrencies(response);
                return;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\":\"Database error: " + e.getMessage() + "\"}");
            }
        }

        String[] parts = path.split("/");
        if (parts.length < 2) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid currency code");
            return;
        }
        String code = parts[1];
        try {
            getCurrencyByCode(code, response);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String code = request.getParameter("code");
        String name = request.getParameter("name");
        String sign = request.getParameter("sign");
        try{
            Currency currency = currencyService.createCurrency(code, name, sign);
            response.setStatus(201);
            objectMapper.writeValue(response.getWriter(), currency);
        } catch (IllegalArgumentException e){
            response.sendError(400, e.getMessage());
        } catch (SQLException e){
            response.sendError(500, e.getMessage());
        }

    }

    private void getAllCurrencies(HttpServletResponse response) throws ClassNotFoundException, SQLException, IOException {
        Connection co = DatabaseManager.getConnection();
        try (co; Statement statement = co.createStatement()) {
            String query = "SELECT Id, Code, FullName, Sign FROM Currencies;";
            ResultSet rs = statement.executeQuery(query);

            List<Currency> currencies = new ArrayList<>();

            while (rs.next()) {
                int id = rs.getInt(1);//можно указать номер столбца, либо полное название
                String code = rs.getString(2);
                String fullName = rs.getString(3);
                String sign = rs.getString(4);
                currencies.add(new Currency(id, code, fullName, sign));
            }
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            ;
            objectMapper.writeValue(response.getWriter(), currencies);
            rs.close();

        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Database error: " + e.getMessage() + "\"}");
        }
    }

    private void getCurrencyByCode(String code, HttpServletResponse response) throws ClassNotFoundException, SQLException, IOException {
        if (code.length() != 3 || !code.matches("[A-Za-z]{3}")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Currency code must be 3 letters");
            return;
        }
        Connection co = DatabaseManager.getConnection();
        try (co; PreparedStatement statement = co.prepareStatement("SELECT Id, Code, FullName, Sign FROM Currencies WHERE Code = ?;")) {
            statement.setString(1, code.toUpperCase());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Currency target = new Currency(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4)
                );
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                objectMapper.writeValue(response.getWriter(), target);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Currency not found");
            }
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }
}
