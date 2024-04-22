package com.kodilla.currencyexchangebackend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@Builder
public class CurrencyDto {

    private Long Id;
    private String code;
    private String name;
    @Builder.Default
    private boolean active = true;
}
