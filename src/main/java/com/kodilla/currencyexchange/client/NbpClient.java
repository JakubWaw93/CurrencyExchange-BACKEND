package com.kodilla.currencyexchange.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kodilla.currencyexchange.domain.Currency;
import com.kodilla.currencyexchange.domain.ExchangeRate;
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
public class NbpClient {

    private final CurrencyService currencyService;
    private final ExchangeRateService exchangeRateService;
    private final WebClient webClient = WebClient.create();
    private final ObjectMapper objectMapper;
    private final ExchangeRateMapper exchangeRateMapper;

    private URI getUriNbp(final String baseCurrencyCode) {
        return UriComponentsBuilder.fromHttpUrl("https://api.nbp.pl/api/exchangerates/rates/a/" + baseCurrencyCode + "/?format=json")
                .build()
                .encode()
                .toUri();
    }

    @Scheduled(fixedRate = 1200000)
    public void updateExchangeRates() {
        List<Currency> currencies = currencyService.getAllStandardCurrencies();
        currencies.forEach(currency -> {
            ExchangeRate exchangeRate = getExchangeRateFromNbpApi(currency.getCode());
            if (exchangeRate != null) {
                ExchangeRate existingExchangeRate = exchangeRateService.getExchangeRateByCurrencyCodes(exchangeRate.getBaseCurrency().getCode(), exchangeRate.getTargetCurrency().getCode());
                if(existingExchangeRate != null) {
                    existingExchangeRate.setRate(exchangeRate.getRate());
                    existingExchangeRate.setLocalDateTime(exchangeRate.getLocalDateTime());
                    exchangeRateService.saveExchangeRate(existingExchangeRate);
                } else {
                    exchangeRateService.saveExchangeRate(exchangeRate);
                }
            }
        });
    }

    public ExchangeRate getExchangeRateFromNbpApi(final String targetCurrencyCode) {
        URI url = getUriNbp(targetCurrencyCode);

        String responseBody = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            NbpExchangeRateResponse response = objectMapper.readValue(responseBody, NbpExchangeRateResponse.class);
            return exchangeRateMapper.mapNbpResponseToExchangeRate(response);
        } catch (Exception e) {
            return null;
        }
    }

}
