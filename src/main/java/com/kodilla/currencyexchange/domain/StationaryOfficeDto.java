package com.kodilla.currencyexchange.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StationaryOfficeDto {

    private long id;
    private String address;
    private String phone;
}
