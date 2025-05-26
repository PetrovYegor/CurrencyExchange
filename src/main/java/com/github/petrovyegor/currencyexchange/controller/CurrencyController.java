package com.github.petrovyegor.currencyexchange.controller;

import com.github.petrovyegor.currencyexchange.dto.CurrencyDTO;
import com.github.petrovyegor.currencyexchange.service.CurrencyService;
import com.github.petrovyegor.currencyexchange.util.ResponsePreparer;
import com.github.petrovyegor.currencyexchange.util.Validator;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CurrencyController extends HttpServlet {
    private final CurrencyService currencyService = new CurrencyService();
    private final ResponsePreparer responsePreparer = new ResponsePreparer();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String code = request.getParameter("code");
        String name = request.getParameter("name");
        String sign = request.getParameter("sign");

        Validator.validateCurrencyPostParameters(code, name, sign);

        try {
            CurrencyDTO currencyDTO = new CurrencyDTO(0, code, name, sign);
            if (currencyService.isCurrencyExists(code)) {
                response.sendError(409, "Currency already exists!");
                return;
            }
            CurrencyDTO currency = currencyService.createCurrency(currencyDTO);//нужна проверка на не null dto
            if (currency != null) {
                responsePreparer.prepareResponse(response, 201);
                responsePreparer.writeValue(response, currency);
            }
        } catch (IllegalArgumentException e) {
            response.sendError(400, e.getMessage());
        } catch (SQLException e) {
            response.sendError(500, e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();
        if (path == null) {
            try {
                List<CurrencyDTO> currencies = currencyService.getAll();
                responsePreparer.prepareResponse(response, 200);
                responsePreparer.writeValue(response, currencies);
                return;
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\":\"Database error: " + e.getMessage() + "\"}");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            Validator.validatePath(path);
            String[] parts = path.split("/");
            String code = parts[1].toUpperCase();
            Validator.validateCode(code);
            CurrencyDTO currencyDTO = currencyService.getByCode(code);
            if (Validator.isNull(currencyDTO)) {
                response.sendError(404, "Currency doesn't exists!");
                return;
            }
            responsePreparer.prepareResponse(response, 200);
            responsePreparer.writeValue(response, currencyDTO);
        } catch (IllegalArgumentException ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid currency code");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
