package com.github.petrovyegor.currencyexchange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.petrovyegor.currencyexchange.dto.CurrencyDTO;
import com.github.petrovyegor.currencyexchange.service.CurrencyService;
import com.github.petrovyegor.currencyexchange.util.Validator;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CurrencyController extends HttpServlet {
    private final CurrencyService currencyService = new CurrencyService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String code = request.getParameter("code");
        String name = request.getParameter("name");
        String sign = request.getParameter("sign");

        if (code == null || name == null || sign == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required fields!");
            return;
        }

        try {
            CurrencyDTO currencyDTO = new CurrencyDTO(0, code.toUpperCase(), name, sign);
            if (currencyService.getByCode(code.toUpperCase()) != null) {
                response.sendError(409, "Currency already exists!");
                return;
            }
            CurrencyDTO currency = currencyService.createCurrency(currencyDTO);//нужна проверка на не null dto
            response.setStatus(201);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            objectMapper.writeValue(response.getWriter(), currency);
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
                response.setStatus(200);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                objectMapper.writeValue(response.getWriter(), currencies);
                return;
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\":\"Database error: " + e.getMessage() + "\"}");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        String[] parts = path.split("/");
        if (parts.length < 2) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid currency code");
            return;
        }
        String code = parts[1].toUpperCase();
        if (!code.matches("[A-Z]{3}")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid currency code");
            return;
        }
        try {
            CurrencyDTO currencyDTO = currencyService.getByCode(code);
            if (Validator.isNull(currencyDTO)) {
                response.sendError(404, "Currency doesn't exists!");
                return;
            }

            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            objectMapper.writeValue(response.getWriter(), currencyDTO);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Database error: " + e.getMessage() + "\"}");

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
