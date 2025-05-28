package com.github.petrovyegor.currencyexchange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.petrovyegor.currencyexchange.dto.ConversionDto;
import com.github.petrovyegor.currencyexchange.dto.CurrencyDto;
import com.github.petrovyegor.currencyexchange.dto.ExchangeRateDto;
import com.github.petrovyegor.currencyexchange.service.ExchangeRateService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

public class ConversionController extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ExchangeRateService exchangeRateService = new ExchangeRateService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();
        if (path != null) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Invalid parameters");
        }
        String baseCurrencyCode = request.getParameter("from");
        String targetCurrencyCode = request.getParameter("to");
        String amountString = request.getParameter("amount");

        if (baseCurrencyCode == null || targetCurrencyCode == null || amountString == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required fields!");
            return;
        }
        try {
            CurrencyDto baseCurrency = exchangeRateService.getCurrencyByCode(baseCurrencyCode);
            CurrencyDto targetCurrency = exchangeRateService.getCurrencyByCode(targetCurrencyCode);
            ExchangeRateDto exchangeRate = exchangeRateService.getByCurrencies(baseCurrency, targetCurrency);//должна быть проверка, что обменный курс не null
            if (exchangeRate != null) {//если есть прямой курс
                double amount = Double.parseDouble(amountString);
                double convertedAmount = amount * exchangeRate.getRate();

                //вынести в сервис заполнение дто?
                ConversionDto conversionDTO = new ConversionDto(exchangeRate.getBaseCurrency(), exchangeRate.getTargetCurrency(), exchangeRate.getRate(), amount, convertedAmount);
                response.setStatus(200);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                objectMapper.writeValue(response.getWriter(), conversionDTO);
            }
            if (exchangeRate == null) {
                exchangeRate = exchangeRateService.getByCurrencies(targetCurrency, baseCurrency);//должна быть проверка, что обменный курс не null
            }
            if (exchangeRate != null) {//если есть обратный курс
                double amount = Double.parseDouble(amountString);
                double newRate = 1 / exchangeRate.getRate();
                double convertedAmount = amount * newRate;

                //вынести в сервис заполнение дто?
                ConversionDto conversionDTO = new ConversionDto(exchangeRate.getBaseCurrency(), exchangeRate.getTargetCurrency(), newRate, amount, convertedAmount);
                response.setStatus(200);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                objectMapper.writeValue(response.getWriter(), conversionDTO);
            }
            CurrencyDto usd = exchangeRateService.getCurrencyByCode("USD");
            if (exchangeRate == null) {
                exchangeRate = exchangeRateService.getByCurrencies(usd, baseCurrency);//должна быть проверка, что обменный курс не null
            }
            if (exchangeRate != null) {//если есть кросс курс
                double amount = Double.parseDouble(amountString);
                ExchangeRateDto resultRate = exchangeRateService.getByCurrencies(usd, targetCurrency);//должна быть проверка, что обменный курс не null
                if (resultRate != null) {//если вторая валюта тоже имеет обменный курс с долларом
                    double newRate = 1 / exchangeRate.getRate() * resultRate.getRate();
                    double convertedAmount = amount * newRate;
                    //вынести в сервис заполнение дто?
                    ConversionDto conversionDTO = new ConversionDto(baseCurrency, targetCurrency, newRate, amount, convertedAmount);
                    response.setStatus(200);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    objectMapper.writeValue(response.getWriter(), conversionDTO);
                }

            }

        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Database error: " + e.getMessage() + "\"}");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
