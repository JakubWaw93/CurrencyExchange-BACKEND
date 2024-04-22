package com.kodilla.currencyexchange.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyDto {

    private long Id;
    private String code;
    private String name;
}
