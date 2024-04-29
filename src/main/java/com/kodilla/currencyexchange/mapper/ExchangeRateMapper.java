package com.kodilla.currencyexchange.mapper;

import com.kodilla.currencyexchange.client.BinanceExchangeRateResponse;
import com.kodilla.currencyexchange.client.NbpExchangeRateResponse;
import com.kodilla.currencyexchange.domain.ExchangeRate;
import com.kodilla.currencyexchange.domain.ExchangeRateDto;
import com.kodilla.currencyexchange.service.CurrencyService;
import com.kodilla.currencyexchange.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ExchangeRateMapper {

    private final ExchangeRateService exchangeRateService;
    private final CurrencyService currencyService;

    public ExchangeRate mapNbpResponseToExchangeRate(NbpExchangeRateResponse response) {
        return ExchangeRate.builder()
                .baseCurrency(currencyService.getCurrencyByCode(response.getCode()))
                .targetCurrency(currencyService.getCurrencyByCode("PLN"))
                .rate(response.getRates().get(0).getMid())
                .lastUpdateTime(LocalDateTime.now())
                .build();
    }

    public ExchangeRate mapBinanceResponseToExchangeRate(final BinanceExchangeRateResponse response, final String baseCurrencyCode) {
        return ExchangeRate.builder()
                .baseCurrency(currencyService.getCurrencyByCode(baseCurrencyCode))
                .targetCurrency(currencyService.getCurrencyByCode("PLN"))
                .rate(new BigDecimal(response.getPrice()).multiply(exchangeRateService.getExchangeRateByCurrencyCodes("USD", "PLN").getRate()))
                .lastUpdateTime(LocalDateTime.now())
                .build();
    }

    public ExchangeRate mapToExchangeRate(final ExchangeRateDto exchangeRateDto) {
        return ExchangeRate.builder()
                .id(exchangeRateDto.getId())
                .baseCurrency(currencyService.getCurrencyById(exchangeRateDto.getBaseCurrencyId()))
                .targetCurrency(currencyService.getCurrencyById(exchangeRateDto.getTargetCurrencyId()))
                .rate(exchangeRateDto.getRate())
                .lastUpdateTime(exchangeRateDto.getLastUpdateTime())
                .build();
    }

    public ExchangeRateDto mapToExchangeRateDto(final ExchangeRate exchangeRate) {
        return ExchangeRateDto.builder()
                .id(exchangeRate.getId())
                .baseCurrencyId(exchangeRate.getBaseCurrency().getId())
                .targetCurrencyId(exchangeRate.getTargetCurrency().getId())
                .rate(exchangeRate.getRate())
                .lastUpdateTime(exchangeRate.getLastUpdateTime())
                .build();
    }

    public List<ExchangeRateDto> mapToExchangeRateDtoList(final List<ExchangeRate> exchangeRateList) {
        return exchangeRateList.stream()
                .map(this::mapToExchangeRateDto)
                .toList();
    }

}
