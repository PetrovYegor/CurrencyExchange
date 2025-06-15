package com.github.petrovyegor.currencyexchange.service;

import com.github.petrovyegor.currencyexchange.dao.ExchangeRateDao;
import com.github.petrovyegor.currencyexchange.dto.CurrencyResponseDto;
import com.github.petrovyegor.currencyexchange.dto.ExchangeRateRequestDto;
import com.github.petrovyegor.currencyexchange.dto.ExchangeRateResponseDto;
import com.github.petrovyegor.currencyexchange.exception.RestErrorException;
import com.github.petrovyegor.currencyexchange.model.ExchangeRate;
import jakarta.servlet.http.HttpServletResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateService {
    private final ExchangeRateDao exchangeRateDao = new ExchangeRateDao();
    private final CurrencyService currencyService = new CurrencyService();

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

    public ExchangeRateResponseDto findByCurrencyConcatenatedCodes(String pair) {
        String baseCode = pair.substring(0, 3);
        String targetCode = pair.substring(3);
        return findByCurrencyCodes(baseCode, targetCode);
    }

    public ExchangeRateResponseDto findByCurrencyCodes(String baseCode, String targetCode) {
        ExchangeRate exchangeRate = exchangeRateDao.findByCurrencyCodes(baseCode, targetCode)
                .orElseThrow(() -> new RestErrorException(HttpServletResponse.SC_NOT_FOUND, "There is no exchange rate with this pair of currency codes"));
        return toDto(exchangeRate);
    }

    public boolean isExchangeRateExists(String baseCode, String targetCode) {
        return exchangeRateDao.findByCurrencyCodes(baseCode, targetCode).isPresent();
    }

    public ExchangeRateResponseDto createExchangeRate(ExchangeRateRequestDto exchangeRateRequestDto) {
        double currentRate = exchangeRateRequestDto.getRate();
        exchangeRateRequestDto.setRate(roundRate(currentRate));
        ExchangeRate exchangeRate = exchangeRateDao.save(toExchangeRate(exchangeRateRequestDto));
        return toDto(exchangeRate);
    }

    public ExchangeRateResponseDto updateRate(ExchangeRateRequestDto exchangeRateRequestDto) {
        double currentRate = exchangeRateRequestDto.getRate();
        exchangeRateRequestDto.setRate(roundRate(currentRate));
        return toDto(exchangeRateDao.updateRate(toExchangeRate(exchangeRateRequestDto)));
    }

    private ExchangeRate toExchangeRate(ExchangeRateRequestDto source) {
        int id = source.getId();
        int baseCurrencyId = currencyService.findByCode(source.getBaseCurrencyCode()).getId();
        int targetCurrencyId = currencyService.findByCode(source.getTargetCurrencyCode()).getId();
        double rate = source.getRate();
        if (source.getId() == 0) {
            return new ExchangeRate(baseCurrencyId, targetCurrencyId, rate);
        }
        return new ExchangeRate(id, baseCurrencyId, targetCurrencyId, rate);
    }

    private double roundRate(double rate) {
        BigDecimal bigDecimal = new BigDecimal(rate).setScale(6, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }
}




