package com.github.petrovyegor.currencyexchange.controller;

import com.github.petrovyegor.currencyexchange.dto.ExchangeRateRequestDto;
import com.github.petrovyegor.currencyexchange.dto.ExchangeRateResponseDto;
import com.github.petrovyegor.currencyexchange.exception.ExchangeRateAlreadyExistsException;
import com.github.petrovyegor.currencyexchange.exception.InvalidParamException;
import com.github.petrovyegor.currencyexchange.exception.RestErrorException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

import static com.github.petrovyegor.currencyexchange.util.RequestAndParametersValidator.validateExchangeRatesPostParameters;
import static com.github.petrovyegor.currencyexchange.util.RequestAndParametersValidator.validateExchangeRatesPostRequest;
import static jakarta.servlet.http.HttpServletResponse.*;

public class ExchangeRatesController extends BaseController {
    private static final String CURRENCY_NOT_FOUND_MESSAGE = "Currency with code '%s' does not exist!";
    private static final String EXCHANGE_RATE_ALREADY_EXISTS_MESSAGE = "Exchange rate with base currency code '%s' and target currency code '%s' already exists!";
    private static final String EQUALS_CODES_MESSAGE = "Currency codes should not be equals";
    private static final String INVALID_RATE_FORMAT_MESSAGE = "Rate must be a positive number with up to 6 decimal places";
    private static final String RATE_FORMAT = "^\\d+(\\.\\d{1,6})?$";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        objectMapper.writeValue(response.getWriter(), exchangeRateService.findAll());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        validateExchangeRatesPostRequest(request);

        String baseCode = request.getParameter("baseCurrencyCode").toUpperCase();
        String targetCode = request.getParameter("targetCurrencyCode").toUpperCase();
        ensureCodesNotEquals(baseCode, targetCode);

        String rateParam = request.getParameter("rate");
        validateRateFormat(rateParam);

        BigDecimal rate = new BigDecimal(rateParam);

        validateExchangeRatesPostParameters(baseCode, targetCode, rate);
        ensureCurrenciesExists(baseCode, targetCode);

        ensureExchangeRateDoesNotExist(baseCode, targetCode);

        ExchangeRateRequestDto exchangeRateRequestDto = new ExchangeRateRequestDto(baseCode, targetCode, rate);
        ExchangeRateResponseDto createdExchangeRate = exchangeRateService.createExchangeRate(exchangeRateRequestDto);
        response.setStatus(SC_CREATED);
        objectMapper.writeValue(response.getWriter(), createdExchangeRate);
    }

    private void ensureCurrenciesExists(String baseCode, String targetCode) {
        if (!currencyService.isCurrencyExists(baseCode)) {
            throw new RestErrorException(HttpServletResponse.SC_NOT_FOUND, CURRENCY_NOT_FOUND_MESSAGE.formatted(baseCode));
        }
        if (!currencyService.isCurrencyExists(targetCode)) {
            throw new RestErrorException(HttpServletResponse.SC_NOT_FOUND, CURRENCY_NOT_FOUND_MESSAGE.formatted(targetCode));
        }
    }

    private void ensureExchangeRateDoesNotExist(String baseCode, String targetCode) {
        if (exchangeRateService.isExchangeRateExists(baseCode, targetCode)) {
            throw new ExchangeRateAlreadyExistsException(SC_CONFLICT, EXCHANGE_RATE_ALREADY_EXISTS_MESSAGE.formatted(baseCode, targetCode));
        }
    }

    private void validateRateFormat(String rate) {
        if (rate == null || !rate.matches(RATE_FORMAT)) {
            throw new InvalidParamException(SC_BAD_REQUEST, INVALID_RATE_FORMAT_MESSAGE);
        }
    }

    private void ensureCodesNotEquals(String baseCode, String targetCode) {
        if (baseCode.equals(targetCode)) {
            throw new InvalidParamException(SC_BAD_REQUEST, EQUALS_CODES_MESSAGE);
        }
    }
}