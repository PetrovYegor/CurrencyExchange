package com.github.petrovyegor.currencyexchange.dto.exchange;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
public class ExchangeRequestDto {
    private String baseCurrencyCode;
    private String targetCurrencyCode;
    private BigDecimal amount;
}
