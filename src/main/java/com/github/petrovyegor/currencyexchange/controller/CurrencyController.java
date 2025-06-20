package com.github.petrovyegor.currencyexchange.controller;

import com.github.petrovyegor.currencyexchange.dto.CurrencyResponseDto;
import com.github.petrovyegor.currencyexchange.exception.InvalidParamException;
import com.github.petrovyegor.currencyexchange.exception.RestErrorException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.github.petrovyegor.currencyexchange.util.RequestParametersValidator.isCodeValid;
import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public class CurrencyController extends BaseController {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();
        if (path == null){
            throw new RestErrorException(SC_BAD_REQUEST, "Unsupported URL given. Missing slash and currency code");
        }
        String code = path.replaceFirst("/", "").toUpperCase();
        if (!isCodeValid(code)) {
            throw new InvalidParamException(SC_BAD_REQUEST, "Code parameter is not valid");
        }
        CurrencyResponseDto currencyDTO = currencyService.findByCode(code);
        objectMapper.writeValue(response.getWriter(), currencyDTO);
    }
}