package com.github.petrovyegor.currencyexchange.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.petrovyegor.currencyexchange.dto.ConversionDTO;
import com.github.petrovyegor.currencyexchange.dto.CurrencyDTO;
import com.github.petrovyegor.currencyexchange.dto.ExchangeRateDTO;
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
            CurrencyDTO baseCurrency = exchangeRateService.getCurrencyByCode(baseCurrencyCode);
            CurrencyDTO targetCurrency = exchangeRateService.getCurrencyByCode(targetCurrencyCode);
            ExchangeRateDTO exchangeRate = exchangeRateService.getByCurrencies(baseCurrency, targetCurrency);
            if (exchangeRate != null){
                double amount = Double.parseDouble(amountString);
                double convertedAmount = amount * exchangeRate.getRate();

                //вынести в сервис заполнение дто?
                ConversionDTO conversionDTO = new ConversionDTO(exchangeRate.getBaseCurrency(), exchangeRate.getTargetCurrency(), exchangeRate.getRate(), amount,convertedAmount);
                response.setStatus(200);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                objectMapper.writeValue(response.getWriter(), conversionDTO);
            }
            if (exchangeRate == null) {
                exchangeRate = exchangeRateService.getByCurrencies(targetCurrency, baseCurrency);
            }
            if (exchangeRate != null){
                double amount = Double.parseDouble(amountString);
                double newRate = 1 / exchangeRate.getRate();
                double convertedAmount = amount * newRate;

                //вынести в сервис заполнение дто?
                ConversionDTO conversionDTO = new ConversionDTO(exchangeRate.getBaseCurrency(), exchangeRate.getTargetCurrency(), newRate, amount,convertedAmount);
                response.setStatus(200);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                objectMapper.writeValue(response.getWriter(), conversionDTO);
            }
            CurrencyDTO usd = exchangeRateService.getCurrencyByCode("USD");
            if (exchangeRate == null){
                exchangeRate = exchangeRateService.getByCurrencies(baseCurrency, usd);
            }
            if (exchangeRate != null){
                double amount = Double.parseDouble(amountString);
                double convertedToUsdAmount = amount * exchangeRate.getRate();
                ExchangeRateDTO resultRate = exchangeRateService.getByCurrencies(usd, targetCurrency);
                double convertedFromUsdAmount = convertedToUsdAmount * resultRate.getRate();
                //вынести в сервис заполнение дто?
                ConversionDTO conversionDTO = new ConversionDTO(exchangeRate.getBaseCurrency(), exchangeRate.getTargetCurrency(), newRate, amount,convertedFromUsdAmount);
                response.setStatus(200);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                objectMapper.writeValue(response.getWriter(), conversionDTO);
            }


//            else {
//                //кинуть ошибку
//            }

        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Database error: " + e.getMessage() + "\"}");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
