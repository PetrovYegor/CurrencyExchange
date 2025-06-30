package com.github.petrovyegor.currencyexchange.controller.currency;

import com.github.petrovyegor.currencyexchange.controller.BaseController;
import com.github.petrovyegor.currencyexchange.dto.currency.CurrencyRequestDto;
import com.github.petrovyegor.currencyexchange.dto.currency.CurrencyResponseDto;
import com.github.petrovyegor.currencyexchange.exception.CurrencyAlreadyExistsException;
import com.github.petrovyegor.currencyexchange.exception.ErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.github.petrovyegor.currencyexchange.util.RequestAndParametersValidator.validateCurrenciesPostParameters;
import static com.github.petrovyegor.currencyexchange.util.RequestAndParametersValidator.validateCurrenciesPostRequest;
import static jakarta.servlet.http.HttpServletResponse.SC_CONFLICT;
import static jakarta.servlet.http.HttpServletResponse.SC_CREATED;

public class CurrenciesController extends BaseController {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        objectMapper.writeValue(response.getWriter(), currencyService.findAll());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        validateCurrenciesPostRequest(request);

        String code = request.getParameter("code").toUpperCase();
        String name = request.getParameter("name");
        String sign = request.getParameter("sign");

        validateCurrenciesPostParameters(code, name, sign);
        ensureCurrencyDoesNotExist(code);
        CurrencyRequestDto currencyRequestDto = new CurrencyRequestDto(code, name, sign);
        CurrencyResponseDto createdCurrency = currencyService.createCurrency(currencyRequestDto);
        response.setStatus(SC_CREATED);
        objectMapper.writeValue(response.getWriter(), createdCurrency);
    }

    private void ensureCurrencyDoesNotExist(String code) {
        if (currencyService.isCurrencyExists(code)) {
            throw new CurrencyAlreadyExistsException(SC_CONFLICT, ErrorMessage.CURRENCY_ALREADY_EXISTS.formatted(code)
            );
        }
    }
}