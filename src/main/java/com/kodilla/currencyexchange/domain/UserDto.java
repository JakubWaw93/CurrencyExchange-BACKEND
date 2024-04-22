package com.kodilla.currencyexchange.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {

    private long id;
    private String firstname;
    private String emailAddress;
    private String apiKey;
    private boolean active;
}
