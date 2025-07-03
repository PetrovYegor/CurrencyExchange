package com.github.petrovyegor.currencyexchange.dto.exchange_rate;

import com.github.petrovyegor.currencyexchange.dto.currency.CurrencyResponseDto;

import java.math.BigDecimal;


public record ExchangeRateResponseDto(int id,
                                      CurrencyResponseDto baseCurrency,
                                      CurrencyResponseDto targetCurrency,
                                      BigDecimal rate) {
}
