package com.github.petrovyegor.currencyexchange.dto.exchange;

import java.math.BigDecimal;


public record ExchangeRequestDto(String baseCurrencyCode, String targetCurrencyCode, BigDecimal amount) {
}
