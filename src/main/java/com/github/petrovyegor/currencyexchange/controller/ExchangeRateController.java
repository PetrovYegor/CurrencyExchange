package com.github.petrovyegor.currencyexchange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.petrovyegor.currencyexchange.dto.ExchangeRateResponseDTO;
import com.github.petrovyegor.currencyexchange.service.ExchangeRateService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRateController extends HttpServlet {
    private final ExchangeRateService exchangeRateService = new ExchangeRateService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String baseCurrencyCode = request.getParameter("baseCurrencyCode");
        String targetCurrencyCode = request.getParameter("targetCurrencyCode");
        String rate = request.getParameter("rate");

        if (baseCurrencyCode == null || targetCurrencyCode == null || rate == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required fields!");
            return;
        }

        try {
            if (!exchangeRateService.isCurrencyExists(baseCurrencyCode) ||
                    !exchangeRateService.isCurrencyExists(targetCurrencyCode)
            ) {
                response.sendError(404, "Base or target, or both currency doesn't exists!");
                return;
            }

            ExchangeRateResponseDTO exchangeRate = exchangeRateService.getByPairOfCodes(baseCurrencyCode.toUpperCase(), targetCurrencyCode.toUpperCase());

            if (exchangeRate != null) {
                response.sendError(409, "Exchange rate already exists!");
                return;
            }

            response.setStatus(201);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            objectMapper.writeValue(response.getWriter(), exchangeRate);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Database error: " + e.getMessage() + "\"}");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();
        if (path == null) {
            try {
                List<ExchangeRateResponseDTO> exchangeRates = exchangeRateService.getAll();
                response.setStatus(200);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                objectMapper.writeValue(response.getWriter(), exchangeRates);
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\":\"Database error: " + e.getMessage() + "\"}");
            }
        }
        String[] parts = path.split("/");
        if (parts.length < 2) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid exchange rate codes");
            return;
        }
        String pairOfCodes = parts[1].toUpperCase();
        if (!pairOfCodes.matches("[A-Z]{6}")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid exchange rate codes");
            return;
        }
        try {
            String baseCurrencyCode = pairOfCodes.substring(0, 3);
            String targetCurrencyCode = pairOfCodes.substring(3);
            if (!exchangeRateService.isCurrencyExists(baseCurrencyCode) ||
                    !exchangeRateService.isCurrencyExists(targetCurrencyCode)
            ) {
                response.sendError(404, "Base or target, or both currency doesn't exists!");
                return;
            }

            ExchangeRateResponseDTO exchangeRate = exchangeRateService.getByPairOfCodes(baseCurrencyCode, targetCurrencyCode);
            if (exchangeRate == null) {
                response.sendError(404, "Exchange rate doesn't exists!");
                return;
            }

            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            objectMapper.writeValue(response.getWriter(), exchangeRate);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Database error: " + e.getMessage() + "\"}");
        }
    }
}
