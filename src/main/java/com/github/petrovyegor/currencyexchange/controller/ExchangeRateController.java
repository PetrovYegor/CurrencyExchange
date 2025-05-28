package com.github.petrovyegor.currencyexchange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.petrovyegor.currencyexchange.dto.CurrencyDto;
import com.github.petrovyegor.currencyexchange.dto.ExchangeRateDto;
import com.github.petrovyegor.currencyexchange.service.ExchangeRateService;
import com.github.petrovyegor.currencyexchange.util.RequestParameterValidator;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/exchangeRates")
public class ExchangeRateController extends HttpServlet {
    private final ExchangeRateService exchangeRateService = new ExchangeRateService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String method = request.getMethod();
        if (method.equals("GET")) {
            this.doGet(request, response);
        } else if (method.equals("POST")) {
            this.doPost(request, response);
        } else if (method.equals("PATCH")) {
            this.doPatch(request, response);
        }
    }

    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String body = request.getReader().lines().collect(Collectors.joining());

        String path = request.getPathInfo();
        if (path == null){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required fields!");
            return;
        }

        String[] parts = path.split("/");

        if (body.isEmpty() || body == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required fields!");
            return;
        }

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
            double rate = Double.parseDouble(body.split("=")[1]);//нужна проверка, что положительное число
            RequestParameterValidator.validateRate(rate);
            String baseCurrencyCode = pairOfCodes.substring(0, 3);
            String targetCurrencyCode = pairOfCodes.substring(3);
            CurrencyDto baseCurrency = exchangeRateService.getCurrencyByCode(baseCurrencyCode);
            CurrencyDto targetCurrency = exchangeRateService.getCurrencyByCode(targetCurrencyCode);

            if (baseCurrency == null || targetCurrency == null) {
                response.sendError(404, "Base or target, or both currency doesn't exists!");
                return;
            }

            ExchangeRateDto exchangeRate = exchangeRateService.getByCurrencies(baseCurrency, targetCurrency);//должна быть проверка, что обменный курс не null
            if (exchangeRate == null) {
                response.sendError(404, "Exchange rate doesn't exists!");
                return;
            }

            exchangeRate = exchangeRateService.updateRate(rate, exchangeRate.getId(), baseCurrency, targetCurrency);
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            objectMapper.writeValue(response.getWriter(), exchangeRate);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Database error: " + e.getMessage() + "\"}");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String baseCurrencyCodeParameter = request.getParameter("baseCurrencyCode");
        String targetCurrencyCodeParameter = request.getParameter("targetCurrencyCode");
        String rateParameter = request.getParameter("rate");

        if (baseCurrencyCodeParameter == null || targetCurrencyCodeParameter == null || rateParameter == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required fields!");
            return;
        }
        String baseCurrencyCode = baseCurrencyCodeParameter.toUpperCase();
        String targetCurrencyCode = targetCurrencyCodeParameter.toUpperCase();
        double rate = Double.parseDouble(rateParameter);
        RequestParameterValidator.validateRate(rate);

        try {
            CurrencyDto baseCurrency = exchangeRateService.getCurrencyByCode(baseCurrencyCode);
            CurrencyDto targetCurrency = exchangeRateService.getCurrencyByCode(targetCurrencyCode);
            if (baseCurrency == null || targetCurrency == null) {
                response.sendError(404, "Base or target, or both currency doesn't exists!");
                return;
            }

            if (exchangeRateService.getByCurrencies(baseCurrency, targetCurrency) != null) {//должна быть проверка, что обменный курс не null
                response.sendError(409, "Exchange rate already exists!");
                return;
            }

            ExchangeRateDto exchangeRate = exchangeRateService.createExchangeRate(baseCurrency, targetCurrency, rate);
            response.setStatus(201);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            objectMapper.writeValue(response.getWriter(), exchangeRate);
        } catch (SQLException | ClassNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Database error: " + e.getMessage() + "\"}");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();
        if (path == null) {
            try {
                List<ExchangeRateDto> exchangeRates = exchangeRateService.getAll();
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
            CurrencyDto baseCurrency = exchangeRateService.getCurrencyByCode(baseCurrencyCode);
            CurrencyDto targetCurrency = exchangeRateService.getCurrencyByCode(targetCurrencyCode);

            if (baseCurrency == null || targetCurrency == null) {
                response.sendError(404, "Base or target, or both currency doesn't exists!");
                return;
            }

            ExchangeRateDto exchangeRate = exchangeRateService.getByCurrencies(baseCurrency, targetCurrency);//должна быть проверка, что обменный курс не null
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
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
