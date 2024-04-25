package com.kodilla.currencyexchange.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;
    private String firstname;
    private String lastname;
    private String emailAddress;
    @Builder.Default
    private List<Long> transactionsIds = new ArrayList<>();
    private String apiKey;
    @Builder.Default
    private boolean active = true;
}
