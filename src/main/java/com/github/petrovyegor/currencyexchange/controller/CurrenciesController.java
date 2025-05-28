package com.github.petrovyegor.currencyexchange.controller;

import com.github.petrovyegor.currencyexchange.dto.CurrencyDto;
import com.github.petrovyegor.currencyexchange.exception.WrongUrlException;
import com.github.petrovyegor.currencyexchange.service.CurrencyService;
import com.github.petrovyegor.currencyexchange.util.RequestPathValidator;
import com.github.petrovyegor.currencyexchange.util.ResponsePreparer;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CurrenciesController extends HttpServlet {
    private final CurrencyService currencyService = new CurrencyService();
    private final ResponsePreparer responsePreparer = new ResponsePreparer();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();
        if (!RequestPathValidator.isPathNull(path)) {
            throw new WrongUrlException("The requested URL does not exists");
        }
        try {
            List<CurrencyDto> currencies = currencyService.getAll();
            responsePreparer.prepareResponse(response, 200);
            responsePreparer.writeValue(response, currencies);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Database error: " + e.getMessage() + "\"}");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
