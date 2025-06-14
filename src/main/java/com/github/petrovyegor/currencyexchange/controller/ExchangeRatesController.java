package com.github.petrovyegor.currencyexchange.controller;

import com.github.petrovyegor.currencyexchange.dto.ExchangeRateRequestDto;
import com.github.petrovyegor.currencyexchange.dto.ExchangeRateResponseDto;
import com.github.petrovyegor.currencyexchange.exception.InvalidParamException;
import com.github.petrovyegor.currencyexchange.exception.InvalidRequestException;
import com.github.petrovyegor.currencyexchange.exception.RestErrorException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Set;

import static com.github.petrovyegor.currencyexchange.util.RequestParametersValidator.isExchangeRatePostParametersValid;
import static jakarta.servlet.http.HttpServletResponse.*;

public class ExchangeRatesController extends BaseController {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        objectMapper.writeValue(response.getWriter(), exchangeRateService.findAll());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!isPostRequestValid(request)) {
            throw new InvalidRequestException(SC_BAD_REQUEST, "One or more http request parameters are missing");
        }
        String baseCode = request.getParameter("baseCurrencyCode").toUpperCase();
        String targetCode = request.getParameter("targetCurrencyCode").toUpperCase();
        double rate;
        try {
            rate = Double.parseDouble(request.getParameter("rate"));
        } catch (NumberFormatException e) {
            throw new InvalidParamException(SC_BAD_REQUEST, "An incorrect value was entered for the rate parameter");
        }

        if (!isExchangeRatePostParametersValid(baseCode, targetCode, rate)) {
            throw new InvalidParamException(SC_BAD_REQUEST, "One or more of the parameters are invalid");
        }

        rate = roundRate(rate);

        if (!currencyService.isCurrencyExists(baseCode) || !currencyService.isCurrencyExists(targetCode)) {
            throw new RestErrorException(HttpServletResponse.SC_NOT_FOUND, "There is no currency with base or target currency code");
        }

        if (exchangeRateService.isExchangeRateExists(baseCode, targetCode)) {
            throw new RestErrorException(SC_CONFLICT, "Exchange rate already exists with such pair of currency codes");
        }

        ExchangeRateRequestDto exchangeRateRequestDto = new ExchangeRateRequestDto(baseCode, targetCode, rate);
        ExchangeRateResponseDto createdExchangeRate = exchangeRateService.createExchangeRate(exchangeRateRequestDto);
        response.setStatus(SC_CREATED);
        objectMapper.writeValue(response.getWriter(), createdExchangeRate);
    }

    private boolean isPostRequestValid(HttpServletRequest request) {
        Map<String, String[]> parameters = request.getParameterMap();
        Set<String> requiredParameters = Set.of("baseCurrencyCode", "targetCurrencyCode", "rate");
        return parameters.keySet().containsAll(requiredParameters);
    }

    private double roundRate(double rate) {
        BigDecimal bigDecimal = new BigDecimal(rate).setScale(6, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }
}