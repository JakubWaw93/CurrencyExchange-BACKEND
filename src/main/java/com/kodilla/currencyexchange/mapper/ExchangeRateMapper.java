package com.kodilla.currencyexchange.mapper;

import com.kodilla.currencyexchange.client.BinanceExchangeRateResponse;
import com.kodilla.currencyexchange.domain.Currency;
import com.kodilla.currencyexchange.domain.ExchangeRate;
import com.kodilla.currencyexchange.client.NbpExchangeRateResponse;
import com.kodilla.currencyexchange.service.CurrencyService;
import com.kodilla.currencyexchange.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
                .localDateTime(LocalDateTime.now())
                .build();
    }

    public ExchangeRate mapBinanceResponseToExchangeRate(final BinanceExchangeRateResponse response, final String baseCurrencyCode) {
        return ExchangeRate.builder()
                .baseCurrency(currencyService.getCurrencyByCode(baseCurrencyCode))
                .targetCurrency(currencyService.getCurrencyByCode("PLN"))
                .rate(new BigDecimal(response.getPrice()).multiply(exchangeRateService.getExchangeRateByCurrencyCodes("USD", "PLN").getRate()))
                .localDateTime(LocalDateTime.now())
                .build();
    }

}
