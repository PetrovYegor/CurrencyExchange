package com.github.petrovyegor.currencyexchange.controller.exchange;

import com.github.petrovyegor.currencyexchange.controller.BaseController;
import com.github.petrovyegor.currencyexchange.dto.exchange.ExchangeRequestDto;
import com.github.petrovyegor.currencyexchange.dto.exchange.ExchangeResponseDto;
import com.github.petrovyegor.currencyexchange.exception.ErrorMessage;
import com.github.petrovyegor.currencyexchange.exception.InvalidParamException;
import com.github.petrovyegor.currencyexchange.exception.RestErrorException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

import static com.github.petrovyegor.currencyexchange.util.RequestAndParametersValidator.validateExchageGetParameters;
import static com.github.petrovyegor.currencyexchange.util.RequestAndParametersValidator.validateExchangeGetRequest;
import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public class ExchangeController extends BaseController {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        validateExchangeGetRequest(request);

        String baseCode = request.getParameter("from");
        String targetCode = request.getParameter("to");
        ensureCodesNotEquals(baseCode, targetCode);

        BigDecimal amount = getAmountFromRequest(request);
        validateExchageGetParameters(baseCode, targetCode, amount);

        ensureCurrenciesExists(baseCode, targetCode);

        ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto(baseCode, targetCode, amount);
        ExchangeResponseDto exchangeResponsedto = exchangeService.convert(exchangeRequestDto);
        objectMapper.writeValue(response.getWriter(), exchangeResponsedto);
    }

    private BigDecimal getAmountFromRequest(HttpServletRequest request) {
        try {
            return new BigDecimal(request.getParameter("amount"));
        } catch (NumberFormatException e) {
            throw new InvalidParamException(SC_BAD_REQUEST, ErrorMessage.INVALID_AMOUNT);
        }
    }

    private void ensureCurrenciesExists(String baseCode, String targetCode) {
        if (!currencyService.isCurrencyExists(baseCode)) {
            throw new RestErrorException(HttpServletResponse.SC_NOT_FOUND, ErrorMessage.CURRENCY_NOT_FOUND.formatted(baseCode));
        }
        if (!currencyService.isCurrencyExists(targetCode)) {
            throw new RestErrorException(HttpServletResponse.SC_NOT_FOUND, ErrorMessage.CURRENCY_NOT_FOUND.formatted(targetCode));
        }
    }

    private void ensureCodesNotEquals(String baseCode, String targetCode) {
        if (baseCode.equals(targetCode)) {
            throw new InvalidParamException(SC_BAD_REQUEST, ErrorMessage.EQUAL_CODES);
        }
    }
}
