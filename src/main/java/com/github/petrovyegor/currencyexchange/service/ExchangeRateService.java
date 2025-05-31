//package com.github.petrovyegor.currencyexchange.service;
//
//import com.github.petrovyegor.currencyexchange.dao.CurrencyDao;
//import com.github.petrovyegor.currencyexchange.dao.ExchangeRateDao;
//import com.github.petrovyegor.currencyexchange.dto.CurrencyDto;
//import com.github.petrovyegor.currencyexchange.dto.ExchangeRateDto;
//import com.github.petrovyegor.currencyexchange.model.Currency;
//import com.github.petrovyegor.currencyexchange.model.ExchangeRate;
//
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ExchangeRateService {
//    private final ExchangeRateDao exchangeRateDao = new ExchangeRateDao();
//    private final CurrencyDao currencyDao = new CurrencyDao();
//
//    public List<ExchangeRateDto> getAll() throws SQLException {
//        List<ExchangeRate> source = exchangeRateDao.getAll();
//        List<ExchangeRateDto> result = new ArrayList<>();
//        for (ExchangeRate exchangeRate : source) {
//            result.add(toDto(exchangeRate));
//        }
//        return result;
//    }
//
//    private ExchangeRateDto toDto(ExchangeRate source) throws SQLException {
//        CurrencyDto baseCurrency = toCurrencyDTO(currencyDao.getById(source.getBaseCurrencyId()));
//        CurrencyDto targetCurrency = toCurrencyDTO(currencyDao.getById(source.getTargetCurrencyId()));
//        return new ExchangeRateDto(source.getId(), baseCurrency, targetCurrency, source.getRate());
//    }
//
//    public ExchangeRateDto getByCurrencies(CurrencyDto baseCurrency, CurrencyDto targetCurrency) throws SQLException {
//        ExchangeRate result = exchangeRateDao.getByCurrenciesIds(baseCurrency.getId(), targetCurrency.getId());
//        return new ExchangeRateDto(result.getId(), baseCurrency, targetCurrency, result.getRate());
//    }
//
//    public ExchangeRateDto createExchangeRate(CurrencyDto baseCurrency, CurrencyDto targetCurrency, double rate) throws SQLException {
//        ExchangeRate result = exchangeRateDao.save(baseCurrency.getId(), targetCurrency.getId(), rate);
//        return new ExchangeRateDto(result.getId(), baseCurrency, targetCurrency, rate);
//    }
//
//    public CurrencyDto getCurrencyByCode(String code) throws SQLException, ClassNotFoundException {
//        return toCurrencyDTO(currencyDao.getByCode(code));
//    }
//
//    public ExchangeRateDto updateRate(double newRate, int exchangeRateId, CurrencyDto baseCurrency, CurrencyDto targetCurrency) throws SQLException {
//        exchangeRateDao.updateRate(newRate, exchangeRateId);
//        return new ExchangeRateDto(exchangeRateId, baseCurrency, targetCurrency, newRate);
//    }
//
//    private CurrencyDto toCurrencyDTO(Currency currency) {
//        return new CurrencyDto(currency.getId(), currency.getCode(), currency.getFullName(), currency.getSign());
//    }
//}
