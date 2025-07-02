package com.github.petrovyegor.currencyexchange.dto.currency;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CurrencyRequestDto {
    private String code;
    private String name;
    private String sign;
}
