package com.github.petrovyegor.currencyexchange.model;

public class Conversion {
    private int baseCurrencyId;
    private int targetCurrencyId;
    private double rate;
    private double amount;
    private double convertedAmount;

    public Conversion(int baseCurrencyId, int targetCurrencyId, double rate, double amount, double convertedAmount){
        this.baseCurrencyId = baseCurrencyId;
        this.targetCurrencyId = targetCurrencyId;
        this.rate = rate;
        this.amount = amount;
        this.convertedAmount = amount;
    }

    public int getBaseCurrency() {
        return baseCurrencyId;
    }

    public int getTargetCurrency() {
        return targetCurrencyId;
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
