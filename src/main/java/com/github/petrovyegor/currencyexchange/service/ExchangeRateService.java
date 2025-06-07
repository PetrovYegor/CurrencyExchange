package com.github.petrovyegor.currencyexchange.service;

import com.github.petrovyegor.currencyexchange.dao.ExchangeRateDao;
import com.github.petrovyegor.currencyexchange.dto.CurrencyResponseDto;
import com.github.petrovyegor.currencyexchange.dto.ExchangeRateResponseDto;
import com.github.petrovyegor.currencyexchange.exception.RestErrorException;
import com.github.petrovyegor.currencyexchange.model.ExchangeRate;
import jakarta.servlet.http.HttpServletResponse;

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

    public ExchangeRateResponseDto findByPairOfCodes(String pair) {
        String baseCurrencyCode = pair.substring(0, 3);
        String targetCurrencyCode = pair.substring(3);
        CurrencyResponseDto baseCurrency = currencyService.findByCode(baseCurrencyCode);
        CurrencyResponseDto targetCurrency = currencyService.findByCode(targetCurrencyCode);
        ExchangeRate exchangeRate = exchangeRateDao.findByCurrencyIds(baseCurrency.getId(), targetCurrency.getId())
                .orElseThrow(() -> new RestErrorException(HttpServletResponse.SC_NOT_FOUND, "There is no exchange rate with this pair of currency codes"));
        return toDto(exchangeRate);
    }
}
//
//    public ExchangeRateResponseDto createExchangeRate(CurrencyDto baseCurrency, CurrencyDto targetCurrency, double rate) throws SQLException {
//        ExchangeRate result = exchangeRateDao.save(baseCurrency.getId(), targetCurrency.getId(), rate);
//        return new ExchangeRateResponseDto(result.getId(), baseCurrency, targetCurrency, rate);
//    }
//
//    public CurrencyDto getCurrencyByCode(String code) throws SQLException, ClassNotFoundException {
//        return toCurrencyDTO(currencyDao.getByCode(code));
//    }
//
//    public ExchangeRateResponseDto updateRate(double newRate, int exchangeRateId, CurrencyDto baseCurrency, CurrencyDto targetCurrency) throws SQLException {
//        exchangeRateDao.updateRate(newRate, exchangeRateId);
//        return new ExchangeRateResponseDto(exchangeRateId, baseCurrency, targetCurrency, newRate);
//    }
//
//
//    private ExchangeRateResponseDto toDTO(ExchangeRate exchangeRate) {
//
//        CurrencyResponseDto baseCurrency = currencyDao.
//        CurrencyResponseDto targetCurrency = toCurrencyDTO(currencyDao.getById(exchangeRate.getTargetCurrencyId()));
//        return new ExchangeRateResponseDto(source.getId(), baseCurrency, targetCurrency, source.getRate());
//        return new ExchangeRateResponseDto(exchangeRate.getId(), exchangeRate.getCode(), exchangeRate.getFullName(), exchangeRate.getSign());
//    }

