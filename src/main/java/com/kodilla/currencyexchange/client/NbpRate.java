package com.kodilla.currencyexchange.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class NbpRate {

    private String no;

    @JsonProperty("effectiveDate")
    private LocalDate effectiveDate;

    private BigDecimal mid;
}
