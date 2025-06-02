package com.github.petrovyegor.currencyexchange.service;

import com.github.petrovyegor.currencyexchange.dao.CurrencyDao;
import com.github.petrovyegor.currencyexchange.dto.CurrencyDto;
import com.github.petrovyegor.currencyexchange.exception.RestErrorException;
import com.github.petrovyegor.currencyexchange.model.Currency;
import jakarta.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;

public class CurrencyService {
    private final CurrencyDao currencyDao = new CurrencyDao();

    //    public CurrencyDto createCurrency(CurrencyDto currencyDTO) throws SQLException {
//        Currency currency = currencyDao.save(toCurrency(currencyDTO));
//        return toCurrencyDTO(currency);
//    }
//
    public List<CurrencyDto> findAll() {
        List<CurrencyDto> result = new ArrayList<>();
        List<Currency> currencies = currencyDao.findAll();
        for (Currency currency : currencies) {
            result.add(toDTO(currency));
        }
        return result;
    }

    public CurrencyDto findByCode(String code) {
        Currency currency = currencyDao.findByCode(code.toUpperCase())
                .orElseThrow(() -> new RestErrorException(HttpServletResponse.SC_NOT_FOUND, "There is no currency with this code"));
        return toDTO(currency);
    }

    private CurrencyDto toDTO(Currency currency) {
        return new CurrencyDto(currency.getId(), currency.getCode(), currency.getFullName(), currency.getSign());
    }
//
//    private Currency toCurrency(CurrencyDto source) {
//        return new Currency(source.getId(), source.getCode(), source.getFullName(), source.getSign());
//    }
//
//    public boolean isCurrencyExists(String code) throws SQLException, ClassNotFoundException {
//        return currencyDao.getByCode(code) != null;
//    }
}
