package com.github.petrovyegor.currencyexchange.controller;

import com.github.petrovyegor.currencyexchange.dto.CurrencyDto;
import com.github.petrovyegor.currencyexchange.util.RequestParameterValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

public class CurrenciesController extends BaseController {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        objectMapper.writeValue(response.getWriter(), currencyService.findAll());
    }

//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws IOException {
//        String code = request.getParameter("code");
//        String name = request.getParameter("name");
//        String sign = request.getParameter("sign");
//
//        RequestParameterValidator.validateCurrencyPostParameters(code, name, sign);
//
//        try {
//            CurrencyDto currencyDTO = new CurrencyDto(0, code, name, sign);
//            if (currencyService.isCurrencyExists(code)) {
//                response.sendError(409, "Currency already exists!");
//                return;
//            }
//            CurrencyDto currency = currencyService.createCurrency(currencyDTO);//нужна проверка на не null dto
//            if (currency != null) {
//                responsePreparer.prepareResponse(response, 201);
//                responsePreparer.writeValue(response, currency);
//            }
//        } catch (IllegalArgumentException e) {
//            response.sendError(400, e.getMessage());
//        } catch (SQLException e) {
//            response.sendError(500, e.getMessage());
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
