package com.github.petrovyegor.currencyexchange.controller;

import com.github.petrovyegor.currencyexchange.dto.CurrencyResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.github.petrovyegor.currencyexchange.util.RequestAndParametersValidator.validateCode;
import static com.github.petrovyegor.currencyexchange.util.RequestAndParametersValidator.validatePath;

public class CurrencyController extends BaseController {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();
        validatePath(path);
        String code = path.replaceFirst("/", "").toUpperCase();
        validateCode(code);
        CurrencyResponseDto currencyDTO = currencyService.findByCode(code);
        objectMapper.writeValue(response.getWriter(), currencyDTO);
    }
}