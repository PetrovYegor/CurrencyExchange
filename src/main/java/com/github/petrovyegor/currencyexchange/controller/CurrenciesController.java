package com.github.petrovyegor.currencyexchange.controller;

import com.github.petrovyegor.currencyexchange.dto.CurrencyResponseDto;
import com.github.petrovyegor.currencyexchange.dto.CurrencyRequestDto;
import com.github.petrovyegor.currencyexchange.exception.InvalidParamException;
import com.github.petrovyegor.currencyexchange.exception.InvalidRequestException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import static com.github.petrovyegor.currencyexchange.util.RequestParametersValidator.isCurrencyGetParametersValid;
import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_CONFLICT;

public class CurrenciesController extends BaseController {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        objectMapper.writeValue(response.getWriter(), currencyService.findAll());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (!isRequestValid(request)) {
            throw new InvalidRequestException(SC_BAD_REQUEST, "One or more http request parameters are missing");
        }
        String code = request.getParameter("code").toUpperCase();
        String name = request.getParameter("name");
        String sign = request.getParameter("sign");

        if (!isCurrencyGetParametersValid(code, name, sign)) {
            throw new InvalidParamException(SC_BAD_REQUEST, "One or more of the parameters are invalid");
        }

        if (currencyService.isCurrencyExists(code)) {
            response.sendError(SC_CONFLICT, "Currency already exists!");
            return;
        }
        CurrencyRequestDto currencyRequestDto = new CurrencyRequestDto(code, name, sign);
        CurrencyResponseDto createdCurrency = currencyService.createCurrency(currencyRequestDto);
        objectMapper.writeValue(response.getWriter(), createdCurrency);
    }

    private boolean isRequestValid(HttpServletRequest request) {
        Map<String, String[]> parameters = request.getParameterMap();
        Set<String> requiredParameters = Set.of("code", "name", "sign");
        return parameters.keySet().containsAll(requiredParameters);
    }
}
