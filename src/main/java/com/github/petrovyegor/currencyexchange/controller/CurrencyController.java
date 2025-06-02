package com.github.petrovyegor.currencyexchange.controller;

import com.github.petrovyegor.currencyexchange.dto.CurrencyDto;
import com.github.petrovyegor.currencyexchange.exception.InvalidParamException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public class CurrencyController extends BaseController {
    private static final String CURRENCY_CODE_PATTERN = "[a-zA-Z]{3}";


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = request.getPathInfo().replaceFirst("/", "").toUpperCase();
        if (!isCodeValid(code)) {
            throw new InvalidParamException(SC_BAD_REQUEST, "Code parameter is not valid");
        }
        CurrencyDto currencyDTO = currencyService.findByCode(code);
        objectMapper.writeValue(response.getWriter(), currencyDTO);
    }

    private boolean isCodeValid(String code) {
        return code.matches(CURRENCY_CODE_PATTERN);
    }
}