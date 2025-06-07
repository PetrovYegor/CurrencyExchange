package com.github.petrovyegor.currencyexchange.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ExchangeRatesController extends BaseController {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        objectMapper.writeValue(response.getWriter(), exchangeRateService.findAll());
    }
}
