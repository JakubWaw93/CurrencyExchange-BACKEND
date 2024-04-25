package com.kodilla.currencyexchange.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class CurrencyDto {

    private Long Id;
    private String code;
    private String name;
    @Builder.Default
    private List<Long> baseExchangeRatesIds = new ArrayList<>();
    @Builder.Default
    private List<Long> targetExchangeRatesIds = new ArrayList<>();
    @Builder.Default
    private boolean active = true;
    private boolean crypto;
}
