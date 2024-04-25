package com.kodilla.currencyexchange.mapper;

import com.kodilla.currencyexchange.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrencyMapper {

    private final CurrencyRepository currencyRepository;


}
