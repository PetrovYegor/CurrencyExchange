package com.github.petrovyegor.currencyexchange.service;

import com.github.petrovyegor.currencyexchange.dto.CurrencyResponseDto;
import com.github.petrovyegor.currencyexchange.dto.ExchangeRateResponseDto;
import com.github.petrovyegor.currencyexchange.dto.ExchangeRequestDto;
import com.github.petrovyegor.currencyexchange.dto.ExchangeResponseDto;
import com.github.petrovyegor.currencyexchange.exception.RestErrorException;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

public class ExchangeService {
    private final static String CROSS_CURRENCY_CODE = "USD";
    private final static int ROUND_PRECISION = 2;
    ExchangeRateService exchangeRateService = new ExchangeRateService();

    public ExchangeResponseDto convertCurrencies(ExchangeRequestDto exchangeRequestDto) {
        String baseCode = exchangeRequestDto.getBaseCurrencyCode();
        String targetCode = exchangeRequestDto.getTargetCurrencyCode();
        double amount = roundAmount(exchangeRequestDto.getAmount());
        if (exchangeRateService.isExchangeRateExists(baseCode, targetCode)) {
            return doDirectConversion(baseCode, targetCode, amount);
        }
        if (exchangeRateService.isExchangeRateExists(targetCode, baseCode)) {
            return doOpposingConversion(baseCode, targetCode, amount);
        }
        if (exchangeRateService.isExchangeRateExists(CROSS_CURRENCY_CODE, baseCode) && exchangeRateService.isExchangeRateExists(CROSS_CURRENCY_CODE, targetCode)) {
            return doCrossConversion(baseCode, targetCode, amount);
        }
        throw new RestErrorException(SC_NOT_FOUND, String.format("There is no direct, opposite or cross exchange rate for currency codes '%s' and '%s'", baseCode, targetCode));
    }

    private ExchangeResponseDto doDirectConversion(String baseCode, String targetCode, double amount) {
        ExchangeRateResponseDto exchangeRate = exchangeRateService.findByCurrencyCodes(baseCode, targetCode);
        CurrencyResponseDto baseCurrency = exchangeRate.getBaseCurrency();
        CurrencyResponseDto targetCurrency = exchangeRate.getTargetCurrency();
        double rate = exchangeRate.getRate();
        double convertedAmount = amount * rate;
        return new ExchangeResponseDto(baseCurrency, targetCurrency, rate, amount, convertedAmount);
    }

    private ExchangeResponseDto doOpposingConversion(String baseCode, String targetCode, double amount) {
        ExchangeRateResponseDto exchangeRate = exchangeRateService.findByCurrencyCodes(targetCode, baseCode);
        CurrencyResponseDto baseCurrency = exchangeRate.getBaseCurrency();
        CurrencyResponseDto targetCurrency = exchangeRate.getTargetCurrency();
        double rate = 1 / exchangeRate.getRate();
        rate = roundRate(rate);
        double convertedAmount = amount * rate;
        return new ExchangeResponseDto(targetCurrency, baseCurrency, rate, amount, convertedAmount);
    }

    private ExchangeResponseDto doCrossConversion(String baseCode, String targetCode, double amount) {
        ExchangeRateResponseDto from = exchangeRateService.findByCurrencyCodes(CROSS_CURRENCY_CODE, baseCode);
        ExchangeRateResponseDto to = exchangeRateService.findByCurrencyCodes(CROSS_CURRENCY_CODE, targetCode);
        double rate = 1 / from.getRate() * to.getRate();
        rate = roundRate(rate);
        double convertedAmount = amount * rate;

        CurrencyResponseDto baseCurrency = from.getTargetCurrency();
        CurrencyResponseDto targetCurrency = to.getTargetCurrency();

        return new ExchangeResponseDto(baseCurrency, targetCurrency, rate, amount, convertedAmount);
    }

    private double roundRate(double rate) {
        BigDecimal bigDecimal = new BigDecimal(rate).setScale(ROUND_PRECISION, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    private double roundAmount(double amount) {
        BigDecimal bigDecimal = new BigDecimal(amount).setScale(ROUND_PRECISION, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }
}
