package com.kodilla.currencyexchange.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kodilla.currencyexchange.domain.Currency;
import com.kodilla.currencyexchange.domain.ExchangeRate;
import com.kodilla.currencyexchange.exception.CurrencyNotFoundException;
import com.kodilla.currencyexchange.exception.ExchangeRateNotFoundException;
import com.kodilla.currencyexchange.mapper.ExchangeRateMapper;
import com.kodilla.currencyexchange.service.CurrencyService;
import com.kodilla.currencyexchange.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BinanceClient {

    private final CurrencyService currencyService;
    private final ExchangeRateService exchangeRateService;
    private final WebClient webClient = WebClient.create();
    private final ObjectMapper objectMapper;
    private final ExchangeRateMapper exchangeRateMapper;

    private URI getUriBinance(final String baseCurrencyCode) {
        return UriComponentsBuilder.fromHttpUrl("https://api.binance.com/api/v3/avgPrice?symbol=" + baseCurrencyCode +"BTCUSDT")
                .build()
                .encode()
                .toUri();
    }

    @Scheduled(fixedRate = 300000)
    public void updateExchangeRates() {
        List<Currency> currencies = currencyService.getAllCryptoCurrencies();
        currencies.forEach(currency -> {
            ExchangeRate exchangeRate = getExchangeRateFromBinanceApi(currency.getCode());
            if (exchangeRate != null) {
                ExchangeRate existingExchangeRate = null;
                try {
                    existingExchangeRate = exchangeRateService.getExchangeRateByCurrencyCodes(exchangeRate.getBaseCurrency().getCode(), exchangeRate.getTargetCurrency().getCode());
                } catch (ExchangeRateNotFoundException | CurrencyNotFoundException e) {
                    throw new RuntimeException(e);
                }
                if(existingExchangeRate != null) {
                    existingExchangeRate.setRate(exchangeRate.getRate());
                    existingExchangeRate.setLastUpdateTime(exchangeRate.getLastUpdateTime());
                    exchangeRateService.saveExchangeRate(existingExchangeRate);
                } else {
                    exchangeRateService.saveExchangeRate(exchangeRate);
                }
            }
        });
    }

    public ExchangeRate getExchangeRateFromBinanceApi(final String targetCurrencyCode) {
        URI url = getUriBinance(targetCurrencyCode);

        String responseBody = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            BinanceExchangeRateResponse response = objectMapper.readValue(responseBody, BinanceExchangeRateResponse.class);
            return exchangeRateMapper.mapBinanceResponseToExchangeRate(response, targetCurrencyCode);
        } catch (Exception e) {
            return null;
        }
    }
}
