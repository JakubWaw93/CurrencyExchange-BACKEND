package com.kodilla.currencyexchange.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kodilla.currencyexchange.configuration.WebClientConfig;
import com.kodilla.currencyexchange.domain.Currency;
import com.kodilla.currencyexchange.domain.ExchangeRate;
import com.kodilla.currencyexchange.exception.CurrencyNotFoundException;
import com.kodilla.currencyexchange.exception.ExchangeRateNotFoundException;
import com.kodilla.currencyexchange.mapper.ExchangeRateMapper;
import com.kodilla.currencyexchange.repository.CurrencyRepository;
import com.kodilla.currencyexchange.service.CurrencyService;
import com.kodilla.currencyexchange.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NbpClient {

    private final CurrencyService currencyService;
    private final ExchangeRateService exchangeRateService;
    private final WebClientConfig webClientConfig;
    private final ObjectMapper objectMapper;
    private final ExchangeRateMapper exchangeRateMapper;
    private final CurrencyRepository currencyRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(NbpClient.class);

    @Scheduled(cron = "0 0/5 * * * ?")
    @Transactional
    public void updateExchangeRates() {
        List<String> tables = Arrays.asList("a", "b", "c");
        List<Currency> currencies = currencyService.getAllStandardCurrencies();
        currencies.forEach(currency -> {
            ExchangeRate exchangeRate = lookForExchangeRateFromNbpApi(tables, currency.getCode());
            if (exchangeRate != null) {
                ExchangeRate existingExchangeRate;
                try {
                    existingExchangeRate = exchangeRateService.getExchangeRateByCurrencyCodes(exchangeRate.getBaseCurrency().getCode(), exchangeRate.getTargetCurrency().getCode());
                } catch (ExchangeRateNotFoundException | CurrencyNotFoundException e) {
                    existingExchangeRate = null;
                }
                if (existingExchangeRate != null) {
                    existingExchangeRate.setRate(exchangeRate.getRate());
                    existingExchangeRate.setLastUpdateTime(exchangeRate.getLastUpdateTime());
                    exchangeRateService.saveExchangeRate(existingExchangeRate);
                } else {
                    exchangeRateService.saveExchangeRate(exchangeRate);
                }
            }
        });
    }

    public ExchangeRate getExchangeRateFromNbpApi(final String table, final String baseCurrencyCode) {
        URI url = getUriNbp(table, baseCurrencyCode);

        checkIfCurrencyExistAndCreate(baseCurrencyCode);

        String responseBody = webClientConfig.webClientBuilder().build().get()
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

    private void checkIfCurrencyExistAndCreate(String baseCurrencyCode) {
        if (!currencyRepository.existsByCode(baseCurrencyCode)) {
            Currency currency = Currency.builder()
                    .code(baseCurrencyCode)
                    .name(baseCurrencyCode + " name update needed.")
                    .crypto(false)
                    .build();
            currencyRepository.save(currency);
        }
    }

    private URI getUriNbp(final String table, final String baseCurrencyCode) {
        return UriComponentsBuilder.fromHttpUrl("https://api.nbp.pl/api/exchangerates/rates/" + table + "/" + baseCurrencyCode + "/?format=json")
                .build()
                .encode()
                .toUri();
    }

    public ExchangeRate lookForExchangeRateFromNbpApi(final List<String> tables, final String baseCurrencyCode) {
        ExchangeRate exchangeRate = null;
        ExchangeRate retrievedExchangeRate = null;
        for (String table : tables) {
            try {
                retrievedExchangeRate = getExchangeRateFromNbpApi(table, baseCurrencyCode);
            } catch (WebClientResponseException e) {
                LOGGER.info("There is no such currency in table: " + table);
            }
            if (retrievedExchangeRate!= null && (exchangeRate==null || exchangeRate.getLastUpdateTime().isBefore(retrievedExchangeRate.getLastUpdateTime()))) {
                exchangeRate = retrievedExchangeRate;
            }
        }
        return exchangeRate;
    }


}
