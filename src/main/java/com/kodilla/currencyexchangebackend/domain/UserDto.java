package com.kodilla.currencyexchangebackend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;
    private String firstname;
    private String lastname;
    private String emailAddress;
    private String apiKey;
    @Builder.Default
    private boolean active = true;
}
