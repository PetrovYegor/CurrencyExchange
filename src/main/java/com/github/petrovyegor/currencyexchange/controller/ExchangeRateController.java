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
import java.util.stream.Collectors;

import static com.github.petrovyegor.currencyexchange.util.RequestParametersValidator.isPairOfCodesValid;
import static com.github.petrovyegor.currencyexchange.util.RequestParametersValidator.isRateValid;
import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public class ExchangeRateController extends BaseController {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String method = request.getMethod();
        if (method.equals("GET")) {
            this.doGet(request, response);
        } else if (method.equals("PATCH")) {
            this.doPatch(request, response);
        }
    }

    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pair = request.getPathInfo().replaceFirst("/", "").toUpperCase();
        if (!isPairOfCodesValid(pair)) {
            throw new InvalidParamException(SC_BAD_REQUEST, "Pair of currency codes parameter is not valid");
        }

        String body = request.getReader().lines().collect(Collectors.joining());//rate=0.22
        if (!isPatchRequestValid(body)) {
            throw new InvalidRequestException(SC_BAD_REQUEST, "Rate request parameter is missing");
        }
        double rate;
        try {
            rate = Double.parseDouble(body.substring(5));
        } catch (NumberFormatException e) {
            throw new InvalidParamException(SC_BAD_REQUEST, "An incorrect value was entered for the rate parameter");
        }

        if (!isRateValid(rate)) {
            throw new InvalidParamException(SC_BAD_REQUEST, "The rate must be greater than 0 and less than 1000");
        }
        String baseCode = pair.substring(0, 3);
        String targetCode = pair.substring(3);
        rate = roundRate(rate);

        if (!currencyService.isCurrencyExists(baseCode) || !currencyService.isCurrencyExists(targetCode)) {
            throw new RestErrorException(HttpServletResponse.SC_NOT_FOUND, "There is no currency with base or target currency code");
        }

        if (!exchangeRateService.isExchangeRateExists(baseCode, targetCode)) {
            throw new RestErrorException(HttpServletResponse.SC_NOT_FOUND, "There is no exchange rate with such pair of currency codes");
        }

        ExchangeRateResponseDto targetExchangeRate = exchangeRateService.findByCurrencyCodes(pair);

        ExchangeRateRequestDto exchangeRateRequestDto = new ExchangeRateRequestDto(targetExchangeRate.getId(), baseCode, targetCode, rate);
        ExchangeRateResponseDto updatedExchangeRate = exchangeRateService.updateRate(exchangeRateRequestDto);
        objectMapper.writeValue(response.getWriter(), updatedExchangeRate);
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

    private boolean isPatchRequestValid(String body) {
        if (body.isEmpty()) {
            return false;
        }
        return body.substring(0, 5).equals("rate=");
    }

    private double roundRate(double rate) {
        BigDecimal bigDecimal = new BigDecimal(rate).setScale(6, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }
}

