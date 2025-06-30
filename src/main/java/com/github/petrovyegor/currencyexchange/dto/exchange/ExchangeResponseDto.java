package com.github.petrovyegor.currencyexchange.dto.exchange;

import com.github.petrovyegor.currencyexchange.dto.currency.CurrencyResponseDto;

import java.math.BigDecimal;

public class ExchangeResponseDto {
    private CurrencyResponseDto baseCurrency;
    private CurrencyResponseDto targetCurrency;
    private BigDecimal rate;
    private BigDecimal amount;
    private BigDecimal convertedAmount;

    public ExchangeResponseDto(CurrencyResponseDto baseCurrency, CurrencyResponseDto targetCurrency, BigDecimal rate, BigDecimal amount, BigDecimal convertedAmount) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
    }

    public CurrencyResponseDto getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(CurrencyResponseDto baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public CurrencyResponseDto getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(CurrencyResponseDto targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(BigDecimal convertedAmount) {
        this.convertedAmount = convertedAmount;
    }
}
