package com.github.petrovyegor.currencyexchange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.petrovyegor.currencyexchange.exception.DBException;
import com.github.petrovyegor.currencyexchange.service.CurrencyService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public abstract class BaseController extends HttpServlet {
    public final ObjectMapper objectMapper = new ObjectMapper();
    public final CurrencyService currencyService = new CurrencyService();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) {
        String method = request.getMethod();
        try {
            if (method.equals("PATCH")) {
                doPatch(request, response);
            } else {
                super.service(request, response);
            }
        } catch (DBException e) {
            sendError(e.getCode(), e.getMessage(), response);
        } catch (Exception e) {
            sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Fatal error", response);
        }
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) {

    }

    private void sendError(int code, String message, HttpServletResponse response) {
        try {
            response.setStatus(code);
            response.getWriter().print(objectMapper.createObjectNode().put("message", message));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
