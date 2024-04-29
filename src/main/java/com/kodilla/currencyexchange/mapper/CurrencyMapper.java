package com.kodilla.currencyexchange.mapper;

import com.kodilla.currencyexchange.domain.Currency;
import com.kodilla.currencyexchange.domain.CurrencyDto;
import com.kodilla.currencyexchange.domain.ExchangeRate;
import com.kodilla.currencyexchange.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CurrencyMapper {

    private final ExchangeRateRepository exchangeRateRepository;

    public Currency mapToCurrency(final CurrencyDto currencyDto) {
        List<ExchangeRate> asBaseList = currencyDto.getBaseExchangeRatesIds().stream()
                .map(exchangeRateRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        List<ExchangeRate> asTargetList = currencyDto.getTargetExchangeRatesIds().stream()
                .map(exchangeRateRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        return Currency.builder()
                .id(currencyDto.getId())
                .code(currencyDto.getCode())
                .name(currencyDto.getName())
                .baseExchangeRates(asBaseList)
                .targetExchangeRates(asTargetList)
                .crypto(currencyDto.isCrypto())
                .active(currencyDto.isActive())
                .build();
    }

    public CurrencyDto mapToCurrencyDto(final Currency currency) {
        List<Long> asBaseIdsList = currency.getBaseExchangeRates().stream()
                .map(ExchangeRate::getId)
                .toList();
        List<Long> asTargetIdsList = currency.getTargetExchangeRates().stream()
                .map(ExchangeRate::getId)
                .toList();
        return CurrencyDto.builder()
                .Id(currency.getId())
                .code(currency.getCode())
                .name(currency.getName())
                .baseExchangeRatesIds(asBaseIdsList)
                .targetExchangeRatesIds(asTargetIdsList)
                .active(currency.isActive())
                .crypto(currency.isCrypto())
                .build();
    }

    public List<CurrencyDto> mapToCurrencyDtoList(final List<Currency> currencyList) {
        return currencyList.stream()
                .map(this::mapToCurrencyDto)
                .toList();
    }


}
