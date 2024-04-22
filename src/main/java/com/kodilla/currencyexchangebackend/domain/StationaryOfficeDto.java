package com.kodilla.currencyexchangebackend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

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
