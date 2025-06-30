package com.github.petrovyegor.currencyexchange.service;

import com.github.petrovyegor.currencyexchange.dao.CurrencyDao;
import com.github.petrovyegor.currencyexchange.dto.currency.CurrencyRequestDto;
import com.github.petrovyegor.currencyexchange.dto.currency.CurrencyResponseDto;
import com.github.petrovyegor.currencyexchange.exception.ErrorMessage;
import com.github.petrovyegor.currencyexchange.exception.RestErrorException;
import com.github.petrovyegor.currencyexchange.model.Currency;
import jakarta.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;

public class CurrencyService {
    private final CurrencyDao currencyDao = new CurrencyDao();

    public CurrencyResponseDto createCurrency(CurrencyRequestDto currencyRequestDto) {
        Currency currency = currencyDao.save(toCurrency(currencyRequestDto));
        return toDTO(currency);
    }

    public List<CurrencyResponseDto> findAll() {
        List<CurrencyResponseDto> result = new ArrayList<>();
        List<Currency> currencies = currencyDao.findAll();
        for (Currency currency : currencies) {
            result.add(toDTO(currency));
        }
        return result;
    }

    public CurrencyResponseDto findByCode(String code) {
        Currency currency = currencyDao.findByCode(code)
                .orElseThrow(() -> new RestErrorException(HttpServletResponse.SC_NOT_FOUND, ErrorMessage.CURRENCY_NOT_FOUND_BY_CODE_MESSAGE.formatted(code)));
        return toDTO(currency);
    }

    public CurrencyResponseDto toDTO(Currency currency) {
        return new CurrencyResponseDto(currency.getId(), currency.getCode(), currency.getName(), currency.getSign());
    }

    private Currency toCurrency(CurrencyRequestDto source) {
        return new Currency(source.getCode(), source.getName(), source.getSign());
    }

    public boolean isCurrencyExists(String code) {
        return currencyDao.findByCode(code).isPresent();
    }

    public CurrencyResponseDto findById(int id) {
        Currency currency = currencyDao.findById(id)
                .orElseThrow(() -> new RestErrorException(HttpServletResponse.SC_NOT_FOUND, ErrorMessage.CURRENCY_NOT_FOUND_BY_ID_MESSAGE.formatted(id)));
        return toDTO(currency);
    }
}
