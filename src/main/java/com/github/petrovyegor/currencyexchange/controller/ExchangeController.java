package com.github.petrovyegor.currencyexchange.controller;

import com.github.petrovyegor.currencyexchange.dto.ExchangeRequestDto;
import com.github.petrovyegor.currencyexchange.dto.ExchangeResponseDto;
import com.github.petrovyegor.currencyexchange.exception.InvalidParamException;
import com.github.petrovyegor.currencyexchange.exception.InvalidRequestException;
import com.github.petrovyegor.currencyexchange.exception.RestErrorException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import static com.github.petrovyegor.currencyexchange.util.RequestAndParametersValidator.isExchangeGetParametersValid;
import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public class ExchangeController extends BaseController {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!isGetRequestValid(request)) {
            throw new InvalidRequestException(SC_BAD_REQUEST, "One or more http request parameters are missing");
        }

        String baseCode = request.getParameter("from");
        String targetCode = request.getParameter("to");

        BigDecimal amount;
        try {
            amount = new BigDecimal(request.getParameter("amount"));
        } catch (NumberFormatException e) {
            throw new InvalidParamException(SC_BAD_REQUEST, "An incorrect value was entered for the amount parameter");
        }

        if (!isExchangeGetParametersValid(baseCode, targetCode, amount)) {
            throw new InvalidParamException(SC_BAD_REQUEST, "One or more of the parameters are invalid");
        }

        if (!currencyService.isCurrencyExists(baseCode) || !currencyService.isCurrencyExists(targetCode)) {
            throw new RestErrorException(HttpServletResponse.SC_NOT_FOUND, "There is no currency with base or target currency code");
        }

        ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto(baseCode, targetCode, amount);
        ExchangeResponseDto exchangeResponsedto = exchangeService.convert(exchangeRequestDto);
        objectMapper.writeValue(response.getWriter(), exchangeResponsedto);
    }

    private boolean isGetRequestValid(HttpServletRequest request) {
        Map<String, String[]> parameters = request.getParameterMap();
        Set<String> requiredParameters = Set.of("from", "to", "amount");
        return parameters.keySet().containsAll(requiredParameters);
    }
}
