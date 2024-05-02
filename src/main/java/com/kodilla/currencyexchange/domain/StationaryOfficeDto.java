package com.kodilla.currencyexchange.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StationaryOfficeDto {

    private Long id;
    private String address;
    private String phone;
    @Builder.Default
    private boolean active = true;

    @Builder
    public StationaryOfficeDto(Long id, String address, String phone, boolean active) {
        this.id = id;
        this.address = address;
        this.phone = phone;
        this.active = true;
    }
}
