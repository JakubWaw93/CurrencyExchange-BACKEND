package com.kodilla.currencyexchange.mapper;

import com.kodilla.currencyexchange.client.BinanceExchangeRateResponse;
import com.kodilla.currencyexchange.client.NbpExchangeRateResponse;
import com.kodilla.currencyexchange.domain.ExchangeRate;
import com.kodilla.currencyexchange.domain.ExchangeRateDto;
import com.kodilla.currencyexchange.domain.Transaction;
import com.kodilla.currencyexchange.exception.CurrencyNotFoundException;
import com.kodilla.currencyexchange.exception.ExchangeRateNotFoundException;
import com.kodilla.currencyexchange.repository.CurrencyRepository;
import com.kodilla.currencyexchange.repository.ExchangeRateRepository;
import com.kodilla.currencyexchange.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ExchangeRateMapper {

    private final ExchangeRateRepository exchangeRateRepository;
    private final CurrencyRepository currencyRepository;
    private final TransactionRepository transactionRepository;

    public ExchangeRate mapNbpResponseToExchangeRate(NbpExchangeRateResponse response) throws CurrencyNotFoundException {
        return ExchangeRate.builder()
                .baseCurrency(currencyRepository.findByCodeAndActiveTrue(response.getCode()).orElseThrow(CurrencyNotFoundException::new))
                .targetCurrency(currencyRepository.findByCodeAndActiveTrue("PLN").orElseThrow(CurrencyNotFoundException::new))
                .rate(response.getRates().get(0).getMid().setScale(10, RoundingMode.HALF_DOWN))
                .lastUpdateTime(LocalDateTime.of(response.getRates().get(0).getEffectiveDate(), LocalTime.of(0,0)))
                .build();
    }

    public ExchangeRate mapBinanceResponseToExchangeRate(final BinanceExchangeRateResponse response, final String baseCurrencyCode) throws CurrencyNotFoundException, ExchangeRateNotFoundException {
        return ExchangeRate.builder()
                .baseCurrency(currencyRepository.findByCodeAndActiveTrue(baseCurrencyCode).orElseThrow(CurrencyNotFoundException::new))
                .targetCurrency(currencyRepository.findByCodeAndActiveTrue("PLN").orElseThrow(CurrencyNotFoundException::new))
                .rate(new BigDecimal(response.getPrice()).setScale(10, RoundingMode.HALF_UP)
                        .multiply(exchangeRateRepository.findByBaseCurrencyCodeAndTargetCurrencyCode("USD", "PLN")
                                .orElseThrow(ExchangeRateNotFoundException::new).getRate()).setScale(10, RoundingMode.HALF_UP))
                .lastUpdateTime(LocalDateTime.now())
                .build();
    }

    public ExchangeRate mapToExchangeRate(final ExchangeRateDto exchangeRateDto) throws CurrencyNotFoundException {
        List<Transaction> transactions = exchangeRateDto.getTransactionsIds().stream()
                .map(transactionRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        return ExchangeRate.builder()
                .id(exchangeRateDto.getId())
                .baseCurrency(currencyRepository.findByIdAndActiveTrue(exchangeRateDto.getBaseCurrencyId()).orElseThrow(CurrencyNotFoundException::new))
                .targetCurrency(currencyRepository.findByIdAndActiveTrue(exchangeRateDto.getTargetCurrencyId()).orElseThrow(CurrencyNotFoundException::new))
                .transactions(transactions)
                .rate(exchangeRateDto.getRate().setScale(10, RoundingMode.HALF_UP))
                .lastUpdateTime(exchangeRateDto.getLastUpdateTime())
                .build();
    }

    public ExchangeRateDto mapToExchangeRateDto(final ExchangeRate exchangeRate) {
        List<Long> transactionsIds = exchangeRate.getTransactions().stream()
                .map(Transaction::getId)
                .toList();
        return ExchangeRateDto.builder()
                .id(exchangeRate.getId())
                .baseCurrencyId(exchangeRate.getBaseCurrency().getId())
                .targetCurrencyId(exchangeRate.getTargetCurrency().getId())
                .transactionsIds(transactionsIds)
                .rate(exchangeRate.getRate().setScale(10, RoundingMode.HALF_UP))
                .lastUpdateTime(exchangeRate.getLastUpdateTime())
                .baseCurrencyCode(exchangeRate.getBaseCurrency().getCode())
                .targetCurrencyCode(exchangeRate.getTargetCurrency().getCode())
                .build();
    }

    public List<ExchangeRateDto> mapToExchangeRateDtoList(final List<ExchangeRate> exchangeRateList) {
        return exchangeRateList.stream()
                .map(this::mapToExchangeRateDto)
                .toList();
    }

}
