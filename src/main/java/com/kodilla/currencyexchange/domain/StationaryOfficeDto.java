package com.kodilla.currencyexchange.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class StationaryOfficeDto {

    private Long id;
    private String address;
    private String phone;
    @Builder.Default
    private boolean active = true;
}
