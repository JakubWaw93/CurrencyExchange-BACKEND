package com.kodilla.currencyexchange.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
public class NbpExchangeRateResponse {

    private String table;

    private String currency;

    private String code;

    private List<NbpRate> rates;
}
