package com.github.petrovyegor.currencyexchange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.petrovyegor.currencyexchange.model.Currency;
import com.github.petrovyegor.currencyexchange.service.CurrencyService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CurrencyController extends HttpServlet {
    private final CurrencyService currencyService = new CurrencyService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();
        if (path == null) {
            try {
                List<Currency> currencies = currencyService.getAllCurrencies();
                objectMapper.writeValue(response.getWriter(), currencies);
                response.setStatus(200);
                return;
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
        if (code.length() != 3 || !code.matches("[A-Za-z]{3}")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid currency code");
            return;
        }
        try {
            Currency currency = currencyService.getByCode(code);
            response.setStatus(200);
            objectMapper.writeValue(response.getWriter(), currency);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Database error: " + e.getMessage() + "\"}");

        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String code = request.getParameter("code");
        String name = request.getParameter("name");
        String sign = request.getParameter("sign");
        try {
            Currency currency = currencyService.createCurrency(code, name, sign);
            response.setStatus(201);
            objectMapper.writeValue(response.getWriter(), currency);
        } catch (IllegalArgumentException e) {
            response.sendError(400, e.getMessage());
        } catch (SQLException e) {
            response.sendError(500, e.getMessage());
        }
    }
}
