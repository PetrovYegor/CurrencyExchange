package com.github.petrovyegor.currencyexchange.controller;

import com.github.petrovyegor.currencyexchange.dto.ExchangeRateResponseDto;
import com.github.petrovyegor.currencyexchange.exception.InvalidParamException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.github.petrovyegor.currencyexchange.util.RequestParametersValidator.isPairOfCodesValid;
import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public class ExchangeRateController extends BaseController {

//    @Override
//    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        String method = request.getMethod();
//        if (method.equals("GET")) {
//            this.doGet(request, response);
//        } else if (method.equals("POST")) {
//            this.doPost(request, response);
//        } else if (method.equals("PATCH")) {
//            this.doPatch(request, response);
//        }
//    }

//    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        String body = request.getReader().lines().collect(Collectors.joining());
//
//        String path = request.getPathInfo();
//        if (path == null) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required fields!");
//            return;
//        }
//
//        String[] parts = path.split("/");
//
//        if (body.isEmpty() || body == null) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required fields!");
//            return;
//        }
//
//        if (parts.length < 2) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid exchange rate codes");
//            return;
//        }
//        String pairOfCodes = parts[1].toUpperCase();
//        if (!pairOfCodes.matches("[A-Z]{6}")) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid exchange rate codes");
//            return;
//        }
//        try {
//            double rate = Double.parseDouble(body.split("=")[1]);//нужна проверка, что положительное число
//            RequestParameterValidator.validateRate(rate);
//            String baseCurrencyCode = pairOfCodes.substring(0, 3);
//            String targetCurrencyCode = pairOfCodes.substring(3);
//            CurrencyDto baseCurrency = exchangeRateService.getCurrencyByCode(baseCurrencyCode);
//            CurrencyDto targetCurrency = exchangeRateService.getCurrencyByCode(targetCurrencyCode);
//
//            if (baseCurrency == null || targetCurrency == null) {
//                response.sendError(404, "Base or target, or both currency doesn't exists!");
//                return;
//            }
//
//            ExchangeRateResponseDto exchangeRate = exchangeRateService.getByCurrencies(baseCurrency, targetCurrency);//должна быть проверка, что обменный курс не null
//            if (exchangeRate == null) {
//                response.sendError(404, "Exchange rate doesn't exists!");
//                return;
//            }
//
//            exchangeRate = exchangeRateService.updateRate(rate, exchangeRate.getId(), baseCurrency, targetCurrency);
//            response.setStatus(200);
//            response.setContentType("application/json");
//            response.setCharacterEncoding("UTF-8");
//            objectMapper.writeValue(response.getWriter(), exchangeRate);
//        } catch (SQLException e) {
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            response.getWriter().write("{\"error\":\"Database error: " + e.getMessage() + "\"}");
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }



//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        String path = request.getPathInfo();
//        if (path == null) {
//            try {
//                List<ExchangeRateResponseDto> exchangeRates = exchangeRateService.getAll();
//                response.setStatus(200);
//                response.setContentType("application/json");
//                response.setCharacterEncoding("UTF-8");
//                objectMapper.writeValue(response.getWriter(), exchangeRates);
//            } catch (SQLException e) {
//                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//                response.getWriter().write("{\"error\":\"Database error: " + e.getMessage() + "\"}");
//            }
//        }
//        String[] parts = path.split("/");
//        if (parts.length < 2) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid exchange rate codes");
//            return;
//        }
//        String pairOfCodes = parts[1].toUpperCase();
//        if (!pairOfCodes.matches("[A-Z]{6}")) {
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid exchange rate codes");
//            return;
//        }
//        try {
//            String baseCurrencyCode = pairOfCodes.substring(0, 3);
//            String targetCurrencyCode = pairOfCodes.substring(3);
//            CurrencyDto baseCurrency = exchangeRateService.getCurrencyByCode(baseCurrencyCode);
//            CurrencyDto targetCurrency = exchangeRateService.getCurrencyByCode(targetCurrencyCode);
//
//            if (baseCurrency == null || targetCurrency == null) {
//                response.sendError(404, "Base or target, or both currency doesn't exists!");
//                return;
//            }
//
//            ExchangeRateResponseDto exchangeRate = exchangeRateService.getByCurrencies(baseCurrency, targetCurrency);//должна быть проверка, что обменный курс не null
//            if (exchangeRate == null) {
//                response.sendError(404, "Exchange rate doesn't exists!");
//                return;
//            }
//
//            response.setStatus(200);
//            response.setContentType("application/json");
//            response.setCharacterEncoding("UTF-8");
//            objectMapper.writeValue(response.getWriter(), exchangeRate);
//        } catch (SQLException e) {
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            response.getWriter().write("{\"error\":\"Database error: " + e.getMessage() + "\"}");
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pair = request.getPathInfo().replaceFirst("/", "").toUpperCase();
        if (!isPairOfCodesValid(pair)) {
            throw new InvalidParamException(SC_BAD_REQUEST, "Pair of currency codes parameter is not valid");
        }
        ExchangeRateResponseDto exchangeRateResponseDto = exchangeRateService.findByCurrencyCodes(pair);
        objectMapper.writeValue(response.getWriter(), exchangeRateResponseDto);
    }
}

