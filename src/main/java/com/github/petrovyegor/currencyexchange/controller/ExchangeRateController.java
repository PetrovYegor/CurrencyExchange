package com.github.petrovyegor.currencyexchange.controller;

import com.github.petrovyegor.currencyexchange.dto.ExchangeRateRequestDto;
import com.github.petrovyegor.currencyexchange.dto.ExchangeRateResponseDto;
import com.github.petrovyegor.currencyexchange.exception.InvalidParamException;
import com.github.petrovyegor.currencyexchange.exception.RestErrorException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

import static com.github.petrovyegor.currencyexchange.util.RequestAndParametersValidator.*;
import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public class ExchangeRateController extends BaseController {
    private static final String RATE_FORMAT = "^\\d+(\\.\\d{1,6})?$";
    private static final String CURRENCY_NOT_FOUND_MESSAGE = "Currency with code '%s' does not exist!";

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
        validateExchangeRatePatchRequest(request);
        String path = request.getPathInfo();
        validatePath(path);
        String pair = path.replaceFirst("/", "").toUpperCase();
        validatePairOfCodes(pair);

        String rateParam = request.getParameter("rate");
        validateRateFormat(rateParam);

        BigDecimal rate = new BigDecimal(rateParam);
        validateExchangeRatesPatchParameters(rate);
        String[] codes = splitCurrencyPair(pair);
        ensureCurrenciesExists(codes[0], codes[1]);

        ExchangeRateResponseDto targetExchangeRate = exchangeRateService.findByCurrencyCodes(codes[0], codes[1]);

        ExchangeRateRequestDto exchangeRateRequestDto = new ExchangeRateRequestDto(targetExchangeRate.getId(), codes[0], codes[1], rate);
        ExchangeRateResponseDto updatedExchangeRate = exchangeRateService.updateRate(exchangeRateRequestDto);
        objectMapper.writeValue(response.getWriter(), updatedExchangeRate);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();
        validatePath(path);
        String pair = path.replaceFirst("/", "").toUpperCase();
        validatePairOfCodes(pair);
        String[] codes = splitCurrencyPair(pair);

        ExchangeRateResponseDto exchangeRateResponseDto = exchangeRateService.findByCurrencyCodes(codes[0], codes[1]);
        objectMapper.writeValue(response.getWriter(), exchangeRateResponseDto);
    }

    private String[] splitCurrencyPair(String pair) {
        return new String[]{
                pair.substring(0, 3),
                pair.substring(3)
        };
    }

    private void ensureCurrenciesExists(String baseCode, String targetCode) {
        if (!currencyService.isCurrencyExists(baseCode)) {
            throw new RestErrorException(HttpServletResponse.SC_NOT_FOUND, CURRENCY_NOT_FOUND_MESSAGE.formatted(baseCode));
        }
        if (!currencyService.isCurrencyExists(targetCode)) {
            throw new RestErrorException(HttpServletResponse.SC_NOT_FOUND, CURRENCY_NOT_FOUND_MESSAGE.formatted(targetCode));
        }
    }

    private void validateRateFormat(String rate) {
        if (rate == null || !rate.matches(RATE_FORMAT)) {
            throw new InvalidParamException(SC_BAD_REQUEST, "Rate must be a positive number with up to 6 decimal places");
        }
    }
}

