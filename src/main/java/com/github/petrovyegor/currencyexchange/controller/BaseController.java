package com.github.petrovyegor.currencyexchange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.petrovyegor.currencyexchange.exception.DBException;
import com.github.petrovyegor.currencyexchange.exception.InvalidParamException;
import com.github.petrovyegor.currencyexchange.exception.InvalidRequestException;
import com.github.petrovyegor.currencyexchange.exception.RestErrorException;
import com.github.petrovyegor.currencyexchange.service.CurrencyService;
import com.github.petrovyegor.currencyexchange.service.ExchangeRateService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public abstract class BaseController extends HttpServlet {
    public final ObjectMapper objectMapper = new ObjectMapper();
    public final CurrencyService currencyService = new CurrencyService();
    public final ExchangeRateService exchangeRateService = new ExchangeRateService();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) {
        String method = request.getMethod();
        try {
            if (method.equals("PATCH")) {
                doPatch(request, response);
            } else {
                super.service(request, response);
            }
        } catch (InvalidParamException e) {
            sendError(e.getCode(), e.getMessage(), response);
        } catch (InvalidRequestException e) {
            sendError(e.getCode(), e.getMessage(), response);
        } catch (RestErrorException e) {
            sendError(e.getCode(), e.getMessage(), response);
        } catch (DBException e) {
            sendError(e.getCode(), e.getMessage(), response);
        } catch (Exception e) {
            sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Fatal error", response);
        }
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException{

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
