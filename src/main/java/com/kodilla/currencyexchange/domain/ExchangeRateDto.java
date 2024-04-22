package com.kodilla.currencyexchange.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
public class ExchangeRateDto {

    private long id;
    private long baseCurrencyId;
    private long targetCurrencyId;
    private BigDecimal rate;
    private Date date;
}
