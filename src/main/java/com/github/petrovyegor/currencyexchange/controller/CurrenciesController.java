package com.github.petrovyegor.currencyexchange.controller;

import com.github.petrovyegor.currencyexchange.dto.CurrencyRequestDto;
import com.github.petrovyegor.currencyexchange.dto.CurrencyResponseDto;
import com.github.petrovyegor.currencyexchange.exception.InvalidParamException;
import com.github.petrovyegor.currencyexchange.exception.InvalidRequestException;
import com.github.petrovyegor.currencyexchange.exception.RestErrorException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import static com.github.petrovyegor.currencyexchange.util.RequestParametersValidator.isCurrencyGetParametersValid;
import static jakarta.servlet.http.HttpServletResponse.*;

public class CurrenciesController extends BaseController {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        objectMapper.writeValue(response.getWriter(), currencyService.findAll());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!isCurrencyRequestValid(request)) {
            throw new InvalidRequestException(SC_BAD_REQUEST, "One or more http request parameters are missing");
        }
        String code = request.getParameter("code").toUpperCase();
        String name = request.getParameter("name");
        String sign = request.getParameter("sign");

        if (!isCurrencyGetParametersValid(code, name, sign)) {
            throw new InvalidParamException(SC_BAD_REQUEST, "One or more of the parameters are invalid");
        }

        if (currencyService.isCurrencyExists(code)) {
            throw new RestErrorException(SC_CONFLICT, "Currency already exists!");
        }
        CurrencyRequestDto currencyRequestDto = new CurrencyRequestDto(code, name, sign);
        CurrencyResponseDto createdCurrency = currencyService.createCurrency(currencyRequestDto);
        response.setStatus(SC_CREATED);
        objectMapper.writeValue(response.getWriter(), createdCurrency);
    }

    private boolean isCurrencyRequestValid(HttpServletRequest request) {
        Map<String, String[]> parameters = request.getParameterMap();
        Set<String> requiredParameters = Set.of("code", "name", "sign");
        return parameters.keySet().containsAll(requiredParameters);
    }
}
