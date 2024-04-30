package com.kodilla.currencyexchange.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class ExchangeRateDto {

    private Long id;
    private Long baseCurrencyId;
    private Long targetCurrencyId;
    @Builder.Default
    private List<Long> transactionsIds = new ArrayList<>();
    private BigDecimal rate;
    private LocalDateTime lastUpdateTime;
}
