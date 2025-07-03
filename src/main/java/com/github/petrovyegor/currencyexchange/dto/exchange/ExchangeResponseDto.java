package com.github.petrovyegor.currencyexchange.dto.exchange;

import com.github.petrovyegor.currencyexchange.dto.currency.CurrencyResponseDto;

import java.math.BigDecimal;


public record ExchangeResponseDto(CurrencyResponseDto baseCurrency, CurrencyResponseDto targetCurrency, BigDecimal rate,
                                  BigDecimal amount, BigDecimal convertedAmount) {
}
