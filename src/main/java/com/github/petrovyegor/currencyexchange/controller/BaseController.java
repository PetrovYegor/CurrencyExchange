package com.github.petrovyegor.currencyexchange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.petrovyegor.currencyexchange.service.CurrencyService;
import com.github.petrovyegor.currencyexchange.service.ExchangeRateService;
import jakarta.servlet.http.HttpServlet;

public abstract class BaseController extends HttpServlet {
    public final ObjectMapper objectMapper = new ObjectMapper();
    public final CurrencyService currencyService = new CurrencyService();
    public final ExchangeRateService exchangeRateService = new ExchangeRateService();
}
