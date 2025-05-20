package com.github.petrovyegor.currencyexchange.service;

import com.github.petrovyegor.currencyexchange.dao.CurrencyDao;
import com.github.petrovyegor.currencyexchange.dto.CurrencyDTO;
import com.github.petrovyegor.currencyexchange.model.Currency;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CurrencyService {
    private final CurrencyDao currencyDao = new CurrencyDao();

    public CurrencyDTO createCurrency(CurrencyDTO currencyDTO) throws SQLException {
        Currency currency =  currencyDao.save(toCurrency(currencyDTO));
        return toCurrencyDTO(currency);
    }

    public List<CurrencyDTO> getAll() throws SQLException, ClassNotFoundException {
        List<CurrencyDTO> result = new ArrayList<>();
        List<Currency> currencies = currencyDao.getAll();
        for (Currency currency : currencies) {
            result.add(toCurrencyDTO(currency));
        }
        return result;
    }

    public CurrencyDTO getByCode(String code) throws SQLException, ClassNotFoundException {
        return toCurrencyDTO(currencyDao.getByCode(code));
    }

    private CurrencyDTO toCurrencyDTO(Currency currency) {
        if (currency != null){
            return new CurrencyDTO(currency.getId(), currency.getCode(), currency.getFullName(), currency.getSign());
        }
        return null;
    }

    private Currency toCurrency(CurrencyDTO source){
        if (source != null){
            return new Currency(source.getId(), source.getCode(), source.getFullName(), source.getSign());
        }
        return null;
    }

}
