package com.github.petrovyegor.currencyexchange.service;

import com.github.petrovyegor.currencyexchange.dao.ExchangeRateDao;
import com.github.petrovyegor.currencyexchange.dto.CurrencyResponseDto;
import com.github.petrovyegor.currencyexchange.dto.ExchangeRateRequestDto;
import com.github.petrovyegor.currencyexchange.dto.ExchangeRateResponseDto;
import com.github.petrovyegor.currencyexchange.exception.RestErrorException;
import com.github.petrovyegor.currencyexchange.model.ExchangeRate;
import jakarta.servlet.http.HttpServletResponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateService {
    private final ExchangeRateDao exchangeRateDao = new ExchangeRateDao();
    private final CurrencyService currencyService = new CurrencyService();
    private static final String EXCHANGE_RATE_DOES_NOT_EXIST_MESSAGE = "Exchange rate with base currency code '%s' and target currency code '%s' does not exist!";

    public List<ExchangeRateResponseDto> findAll() {
        List<ExchangeRateResponseDto> result = new ArrayList<>();
        List<ExchangeRate> exchangeRates = exchangeRateDao.findAll();
        for (ExchangeRate exchangeRate : exchangeRates) {
            result.add(toDto(exchangeRate));
        }
        return result;
    }

    private ExchangeRateResponseDto toDto(ExchangeRate exchangeRate) {
        CurrencyResponseDto baseCurrency = currencyService.findById(exchangeRate.getBaseCurrencyId());
        CurrencyResponseDto targetCurrency = currencyService.findById(exchangeRate.getTargetCurrencyId());
        return new ExchangeRateResponseDto(exchangeRate.getId(), baseCurrency, targetCurrency, exchangeRate.getRate());
    }

    public ExchangeRateResponseDto findByCurrencyCodes(String baseCode, String targetCode) {
        ExchangeRate exchangeRate = exchangeRateDao.findByCurrencyCodes(baseCode, targetCode)
                .orElseThrow(() -> new RestErrorException(HttpServletResponse.SC_NOT_FOUND, EXCHANGE_RATE_DOES_NOT_EXIST_MESSAGE.formatted(baseCode, targetCode)));
        return toDto(exchangeRate);
    }

    public boolean isExchangeRateExists(String baseCode, String targetCode) {
        return exchangeRateDao.findByCurrencyCodes(baseCode, targetCode).isPresent();
    }

    public ExchangeRateResponseDto createExchangeRate(ExchangeRateRequestDto exchangeRateRequestDto) {
        ExchangeRate exchangeRate = exchangeRateDao.save(toExchangeRate(exchangeRateRequestDto));
        return toDto(exchangeRate);
    }

    public ExchangeRateResponseDto updateRate(ExchangeRateRequestDto exchangeRateRequestDto) {
        return toDto(exchangeRateDao.updateRate(toExchangeRate(exchangeRateRequestDto)));
    }

    private ExchangeRate toExchangeRate(ExchangeRateRequestDto source) {
        int id = source.getId();
        int baseCurrencyId = currencyService.findByCode(source.getBaseCurrencyCode()).getId();
        int targetCurrencyId = currencyService.findByCode(source.getTargetCurrencyCode()).getId();
        BigDecimal rate = source.getRate();
        if (isExchangeRateNew(source)) {
            return new ExchangeRate(baseCurrencyId, targetCurrencyId, rate);
        }
        return new ExchangeRate(id, baseCurrencyId, targetCurrencyId, rate);
    }

    private boolean isExchangeRateNew(ExchangeRateRequestDto source) {
        return source.getId() == 0;
    }
}




