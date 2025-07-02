package com.github.petrovyegor.currencyexchange.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Currency {
    private int id;
    private String code;
    private String name;
    private String sign;

    public Currency(String code, String name, String sign) {
        this.code = code;
        this.name = name;
        this.sign = sign;
    }
}
