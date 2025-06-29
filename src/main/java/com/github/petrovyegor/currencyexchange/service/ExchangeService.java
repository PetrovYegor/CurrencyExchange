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
    private static final String CROSS_CURRENCY_CODE = "USD";
    private static final int ROUND_PRECISION = 2;
    private static final BigDecimal RATE_CALCULATION_DIVIDENT = new BigDecimal(1);
    private static final int DIVISION_PRECISION = 2;
    private static final int BIGDECIMAL_PRECISION = 2;
    private final ExchangeRateService exchangeRateService = new ExchangeRateService();
    private static final String UNSUPPORTED_CONVERSION_OPERATION = "There is no direct, opposite or cross exchange rate for currency codes '%s' and '%s'";

    public ExchangeResponseDto convert(ExchangeRequestDto exchangeRequestDto) {
        String baseCode = exchangeRequestDto.getBaseCurrencyCode();
        String targetCode = exchangeRequestDto.getTargetCurrencyCode();
        BigDecimal amount = roundAmount(exchangeRequestDto.getAmount());
        if (isDirectConversion(baseCode, targetCode)) {
            return doDirectConversion(baseCode, targetCode, amount);
        }
        if (isReversedConversion(baseCode, targetCode)) {
            return doReverseConversion(baseCode, targetCode, amount);
        }
        if (isCrossConversion(baseCode, targetCode)) {
            return doCrossConversion(baseCode, targetCode, amount);
        }
        throw new RestErrorException(SC_NOT_FOUND, String.format(UNSUPPORTED_CONVERSION_OPERATION, baseCode, targetCode));
    }

    private ExchangeResponseDto doDirectConversion(String baseCode, String targetCode, BigDecimal amount) {
        ExchangeRateResponseDto exchangeRate = exchangeRateService.findByCurrencyCodes(baseCode, targetCode);
        CurrencyResponseDto baseCurrency = exchangeRate.getBaseCurrency();
        CurrencyResponseDto targetCurrency = exchangeRate.getTargetCurrency();
        BigDecimal rate = exchangeRate.getRate();
        BigDecimal convertedAmount = amount.multiply(rate);
        return new ExchangeResponseDto(baseCurrency, targetCurrency, rate, amount, convertedAmount);
    }

    private ExchangeResponseDto doReverseConversion(String baseCode, String targetCode, BigDecimal amount) {
        ExchangeRateResponseDto exchangeRate = exchangeRateService.findByCurrencyCodes(targetCode, baseCode);
        CurrencyResponseDto baseCurrency = exchangeRate.getBaseCurrency();
        CurrencyResponseDto targetCurrency = exchangeRate.getTargetCurrency();
        BigDecimal rate = getRateForReverseConversion(exchangeRate.getRate());
        BigDecimal convertedAmount = amount.multiply(rate);
        return new ExchangeResponseDto(targetCurrency, baseCurrency, rate, amount, convertedAmount);
    }

    private ExchangeResponseDto doCrossConversion(String baseCode, String targetCode, BigDecimal amount) {
        ExchangeRateResponseDto sourcexchangeRate = exchangeRateService.findByCurrencyCodes(CROSS_CURRENCY_CODE, baseCode);
        ExchangeRateResponseDto targetExchangeRate = exchangeRateService.findByCurrencyCodes(CROSS_CURRENCY_CODE, targetCode);
        BigDecimal baseCurrencyRate = sourcexchangeRate.getRate();
        BigDecimal targetCurrencyRate = targetExchangeRate.getRate();
        BigDecimal rate = getRateForCrossConversion(baseCurrencyRate, targetCurrencyRate);
        BigDecimal convertedAmount = amount.multiply(rate);

        CurrencyResponseDto baseCurrency = sourcexchangeRate.getTargetCurrency();
        CurrencyResponseDto targetCurrency = targetExchangeRate.getTargetCurrency();

        return new ExchangeResponseDto(baseCurrency, targetCurrency, rate, amount, convertedAmount);
    }

    private BigDecimal roundAmount(BigDecimal amount) {
        amount.setScale(ROUND_PRECISION, RoundingMode.HALF_UP);
        return amount;
    }

    private boolean isDirectConversion(String baseCode, String targetCode) {
        return exchangeRateService.isExchangeRateExists(baseCode, targetCode);
    }

    private boolean isReversedConversion(String baseCode, String targetCode) {
        return exchangeRateService.isExchangeRateExists(targetCode, baseCode);
    }

    private boolean isCrossConversion(String baseCode, String targetCode) {
        return exchangeRateService.isExchangeRateExists(CROSS_CURRENCY_CODE, baseCode) && exchangeRateService.isExchangeRateExists(CROSS_CURRENCY_CODE, targetCode);
    }

    private BigDecimal getRateForReverseConversion(BigDecimal rate) {
        return RATE_CALCULATION_DIVIDENT.divide(rate, DIVISION_PRECISION, RoundingMode.HALF_UP);
    }

    private BigDecimal getRateForCrossConversion(BigDecimal baseCurrencyRate, BigDecimal targetCurrencyRate) {
        return RATE_CALCULATION_DIVIDENT.divide(baseCurrencyRate.multiply(targetCurrencyRate), BIGDECIMAL_PRECISION, RoundingMode.HALF_UP);
    }
}
