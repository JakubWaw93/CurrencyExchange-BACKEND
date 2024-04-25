package com.kodilla.currencyexchange.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class BinanceExchangeRateResponse {

    @JsonProperty("mins")
    private int minutes;

    @JsonProperty("price")
    private String price;

    @JsonProperty("closeTime")
    private long closeTime;

}
