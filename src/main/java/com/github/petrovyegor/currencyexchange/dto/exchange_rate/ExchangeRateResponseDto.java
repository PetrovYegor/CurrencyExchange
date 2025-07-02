package com.github.petrovyegor.currencyexchange.dto.exchange_rate;

import com.github.petrovyegor.currencyexchange.dto.currency.CurrencyResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
public class ExchangeRateResponseDto {

    private int id;
    private CurrencyResponseDto baseCurrency;
    private CurrencyResponseDto targetCurrency;
    private BigDecimal rate;
}
