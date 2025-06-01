package com.github.petrovyegor.currencyexchange.controller;

import com.github.petrovyegor.currencyexchange.dto.CurrencyDto;
import com.github.petrovyegor.currencyexchange.util.RequestParameterValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class CurrencyController extends BaseController {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();

        try {
            RequestParameterValidator.validatePath(path);
            String[] parts = path.split("/");
            String code = parts[1].toUpperCase();
            RequestParameterValidator.validateCode(code);
            CurrencyDto currencyDTO = currencyService.getByCode(code);
            if (RequestParameterValidator.isNull(currencyDTO)) {
                response.sendError(404, "Currency doesn't exists!");
            }

        }
    }
}
