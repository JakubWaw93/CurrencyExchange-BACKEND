package com.kodilla.currencyexchange.client;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
@Data
public class BinanceClient {

    private final CurrencyService currencyService;
    private final ExchangeRateService exchangeRateService;
    private final WebClientConfig webClientConfig;
    private final ObjectMapper objectMapper;
    private final ExchangeRateMapper exchangeRateMapper;
    private final CurrencyRepository currencyRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(BinanceClient.class);

    @Value("${binance.api.base.url}")
    private String binanceApiBaseUrl;


    @Scheduled(cron = "0 0/5 * * * ?")
    @Transactional
    public void updateExchangeRates() {
        List<Currency> currencies = currencyService.getAllCryptoCurrencies();
        currencies.forEach(currency -> {
            ExchangeRate exchangeRate = getExchangeRateFromBinanceApi(currency.getCode());
            if (exchangeRate != null) {
                ExchangeRate existingExchangeRate;
                try {
                    existingExchangeRate = exchangeRateService.getExchangeRateByCurrencyCodes(exchangeRate.getBaseCurrency().getCode(), exchangeRate.getTargetCurrency().getCode());
                } catch (ExchangeRateNotFoundException | CurrencyNotFoundException e) {
                    existingExchangeRate=null;
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

    public ExchangeRate getExchangeRateFromBinanceApi(final String baseCurrencyCode) {
        URI url = getUriBinance(baseCurrencyCode);

        checkIfCurrencyExistAndCreate(baseCurrencyCode);

        String responseBody = webClientConfig.webClientBuilder().build().get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        try {
            BinanceExchangeRateResponse response = objectMapper.readValue(responseBody, BinanceExchangeRateResponse.class);
            return exchangeRateMapper.mapBinanceResponseToExchangeRate(response, baseCurrencyCode);
        } catch (JsonProcessingException | CurrencyNotFoundException | ExchangeRateNotFoundException e) {
            LOGGER.error("Retrieving response with exchange rate failed.");
            return null;
        }


    }

    private void checkIfCurrencyExistAndCreate(String baseCurrencyCode) {
        if (!currencyRepository.existsByCode(baseCurrencyCode)) {
            Currency currency = Currency.builder()
                    .code(baseCurrencyCode)
                    .name(baseCurrencyCode + " name update needed.")
                    .crypto(true)
                    .build();
            currencyRepository.save(currency);
        }
    }

    private URI getUriBinance(final String baseCurrencyCode) {
        return UriComponentsBuilder.fromHttpUrl(binanceApiBaseUrl + baseCurrencyCode + "USDT")
                .build()
                .encode()
                .toUri();
    }
}
