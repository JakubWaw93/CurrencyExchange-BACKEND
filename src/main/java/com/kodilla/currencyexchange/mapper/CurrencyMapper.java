package com.kodilla.currencyexchange.mapper;

import com.kodilla.currencyexchange.domain.Currency;
import com.kodilla.currencyexchange.domain.CurrencyDto;
import com.kodilla.currencyexchange.domain.ExchangeRate;
import com.kodilla.currencyexchange.domain.Transaction;
import com.kodilla.currencyexchange.repository.ExchangeRateRepository;
import com.kodilla.currencyexchange.repository.TransactionRepository;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CurrencyMapper {

    private final ExchangeRateRepository exchangeRateRepository;
    private final TransactionRepository transactionRepository;

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
        List<Transaction> transactionsWhenBought = currencyDto.getBoughtInTransactionsIds().stream()
                .map(transactionRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        List<Transaction> transactionsWhenSold = currencyDto.getSoldInTransactionsIds().stream()
                .map(transactionRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        return Currency.builder()
                .id(currencyDto.getId())
                .code(currencyDto.getCode())
                .name(currencyDto.getName())
                .baseExchangeRates(asBaseList)
                .targetExchangeRates(asTargetList)
                .boughtInTransactions(transactionsWhenBought)
                .soldInTransactions(transactionsWhenSold)
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
        List<Long> transactionsWhenBoughtIds = currency.getBoughtInTransactions().stream()
                .map(Transaction::getId)
                .toList();
        List<Long> transactionsWhenSoldIds = currency.getSoldInTransactions().stream()
                .map(Transaction::getId)
                .toList();
        return CurrencyDto.builder()
                .id(currency.getId())
                .code(currency.getCode())
                .name(currency.getName())
                .baseExchangeRatesIds(asBaseIdsList)
                .targetExchangeRatesIds(asTargetIdsList)
                .boughtInTransactionsIds(transactionsWhenBoughtIds)
                .soldInTransactionsIds(transactionsWhenSoldIds)
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
