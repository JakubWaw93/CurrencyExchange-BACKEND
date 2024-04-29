package com.kodilla.currencyexchange.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class ExchangeRateDto {

    private Long id;
    private Long baseCurrencyId;
    private Long targetCurrencyId;
    private BigDecimal rate;
    private LocalDateTime lastUpdateTime;
}
