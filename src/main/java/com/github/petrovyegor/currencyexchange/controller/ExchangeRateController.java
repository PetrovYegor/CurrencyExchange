package com.github.petrovyegor.currencyexchange.controller;

import com.github.petrovyegor.currencyexchange.dto.ExchangeRateResponseDto;
import com.github.petrovyegor.currencyexchange.exception.InvalidParamException;
import com.github.petrovyegor.currencyexchange.exception.InvalidRequestException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.github.petrovyegor.currencyexchange.util.RequestParametersValidator.isPairOfCodesValid;
import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public class ExchangeRateController extends BaseController {
    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String body = request.getReader().lines().collect(Collectors.joining());
        if (!isExchangeRatePatchRequestValid(request)) {
            throw new InvalidRequestException(SC_BAD_REQUEST, "One or more http request parameters are missing");
        }

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
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pair = request.getPathInfo().replaceFirst("/", "").toUpperCase();
        if (!isPairOfCodesValid(pair)) {
            throw new InvalidParamException(SC_BAD_REQUEST, "Pair of currency codes parameter is not valid");
        }
        ExchangeRateResponseDto exchangeRateResponseDto = exchangeRateService.findByCurrencyCodes(pair);
        objectMapper.writeValue(response.getWriter(), exchangeRateResponseDto);
    }

    private boolean isExchangeRatePatchRequestValid(HttpServletRequest request) {
        Map<String, String[]> parameters = request.getParameterMap();
        Set<String> requiredParameters = Set.of("baseCurrencyCode", "targetCurrencyCode", "rate");
        return parameters.keySet().containsAll(requiredParameters);
    }
}

