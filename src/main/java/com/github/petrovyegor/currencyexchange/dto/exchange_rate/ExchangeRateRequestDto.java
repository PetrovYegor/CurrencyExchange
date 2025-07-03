package com.github.petrovyegor.currencyexchange.dto.exchange_rate;

import java.math.BigDecimal;


public record ExchangeRateRequestDto(int id, String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {

    public ExchangeRateRequestDto(String baseCurrencyCode, String targetCurrencyCode, BigDecimal rate) {
        this(0, baseCurrencyCode, targetCurrencyCode, rate);
    }
}
