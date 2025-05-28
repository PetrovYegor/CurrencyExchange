package com.github.petrovyegor.currencyexchange.dto;

public class ConversionDto {
    private CurrencyDto baseCurrency;
    private CurrencyDto targetCurrency;
    private double rate;
    private double amount;
    private double convertedAmount;

    public ConversionDto(CurrencyDto baseCurrency, CurrencyDto targetCurrency, double rate, double amount, double convertedAmount) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
    }

    public CurrencyDto getBaseCurrency() {
        return baseCurrency;
    }

    public CurrencyDto getTargetCurrency() {
        return targetCurrency;
    }

    public double getRate() {
        return rate;
    }

    public double getAmount() {
        return amount;
    }

    public double getConvertedAmount() {
        return convertedAmount;
    }
}
