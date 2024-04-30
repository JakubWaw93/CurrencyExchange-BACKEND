package com.kodilla.currencyexchange.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NbpRate {

    @JsonProperty("no")
    private String no;

    @JsonProperty("effectiveDate")
    private LocalDate effectiveDate;

    @JsonProperty("mid")
    private BigDecimal mid;
}
