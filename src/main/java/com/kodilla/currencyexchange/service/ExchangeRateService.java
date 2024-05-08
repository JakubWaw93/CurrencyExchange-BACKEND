package com.kodilla.currencyexchange.service;

import com.kodilla.currencyexchange.client.BinanceClient;
import com.kodilla.currencyexchange.client.NbpClient;
import com.kodilla.currencyexchange.domain.Currency;
import com.kodilla.currencyexchange.domain.ExchangeRate;
import com.kodilla.currencyexchange.exception.CurrencyNotFoundException;
import com.kodilla.currencyexchange.exception.ExchangeRateNotFoundException;
import com.kodilla.currencyexchange.repository.CurrencyRepository;
import com.kodilla.currencyexchange.repository.ExchangeRateRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;
    private final CurrencyRepository currencyRepository;

    @Autowired
    private EntityManager entityManager;
    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeRateService.class);

    public List<ExchangeRate> getAllExchangeRates() {
        return exchangeRateRepository.findAll();
    }

    public ExchangeRate getExchangeRateById(final Long id) throws ExchangeRateNotFoundException {
        return exchangeRateRepository.findById(id).orElseThrow(ExchangeRateNotFoundException::new);
    }

    public ExchangeRate getExchangeRateByCurrencyCodes(final String baseCurrencyCode, final String targetCurrencyCode) throws ExchangeRateNotFoundException, CurrencyNotFoundException {
        return exchangeRateRepository.findByBaseCurrencyCodeAndTargetCurrencyCode
                (baseCurrencyCode, targetCurrencyCode).orElseThrow
                (ExchangeRateNotFoundException::new);
    }

    public List<ExchangeRate> getExchangeRatesByBaseCurrencyCode(final String baseCurrencyCode) {
        return exchangeRateRepository.findAllByBaseCurrencyCode(baseCurrencyCode);
    }

    public List<ExchangeRate> getExchangeRatesByTargetCurrencyCode(final String targetCurrencyCode) {
        return exchangeRateRepository.findAllByTargetCurrencyCode(targetCurrencyCode);
    }

    public ExchangeRate saveExchangeRate(final ExchangeRate exchangeRate) {
        if (!entityManager.contains(exchangeRate.getBaseCurrency())) {
            exchangeRate.setBaseCurrency(entityManager.merge(exchangeRate.getBaseCurrency()));
        }
        if (!entityManager.contains(exchangeRate.getTargetCurrency())) {
            exchangeRate.setTargetCurrency(entityManager.merge(exchangeRate.getTargetCurrency()));
        }
        return exchangeRateRepository.save(exchangeRate);
    }



    @Scheduled(cron = "0 0/10 * * * ?")
    @Transactional
    public void calculateAndSaveMissingExchangeRates() throws CurrencyNotFoundException {
        List<ExchangeRate> updates = new ArrayList<>();
        List<ExchangeRate> plnExchangeRates = exchangeRateRepository.findAllByTargetCurrencyCode("PLN");

        for (ExchangeRate plnExchangeRate : plnExchangeRates) {
            Currency baseCurrency = plnExchangeRate.getBaseCurrency();
            List<Currency> targetCurrencies = currencyRepository.findAllByCodeNot("PLN");

            for (Currency targetCurrency : targetCurrencies) {
                try {
                    updates.add(createOrUpdateExchangeRate(plnExchangeRate, targetCurrency, baseCurrency));
                } catch (IllegalStateException e) {
                    LOGGER.error("Error processing rate for " + baseCurrency.getCode() + " to " + targetCurrency.getCode(), e);
                }
            }
        }
        exchangeRateRepository.saveAll(updates);
    }

    private ExchangeRate createOrUpdateExchangeRate(ExchangeRate plnExchangeRate, Currency targetCurrency, Currency baseCurrency) {
        BigDecimal newRate = calculateNewExchangeRate(plnExchangeRate, targetCurrency);
        return exchangeRateRepository.findByBaseCurrencyCodeAndTargetCurrencyCode(baseCurrency.getCode(), targetCurrency.getCode())
                .map(existingRate -> {
                    existingRate.setRate(newRate);
                    return existingRate;
                }).orElseGet(() -> ExchangeRate.builder()
                        .rate(newRate)
                        .lastUpdateTime(LocalDateTime.now())
                        .baseCurrency(baseCurrency)
                        .targetCurrency(targetCurrency)
                        .build());
    }


    private BigDecimal calculateNewExchangeRate(ExchangeRate plnExchangeRate, Currency targetCurrency) {
        BigDecimal plnRate = plnExchangeRate.getRate();

        BigDecimal inversePlnRate = BigDecimal.ONE.divide(plnRate, 10, RoundingMode.HALF_UP);

        ExchangeRate targetToPlnExchangeRate = exchangeRateRepository
                .findByBaseCurrencyCodeAndTargetCurrencyCode(targetCurrency.getCode(), "PLN")
                .orElseThrow(() -> new IllegalStateException("Exchange rate not found for " + targetCurrency.getCode() + " to PLN"));

        BigDecimal result = targetToPlnExchangeRate.getRate().multiply(inversePlnRate).setScale(10, RoundingMode.HALF_UP);

        return BigDecimal.ONE.divide(result, 10, RoundingMode.HALF_UP).setScale(10, RoundingMode.HALF_UP);
    }

    @PostConstruct
    private void addNecessaryCurrencies() {
        if (!currencyRepository.existsByCode("PLN")) {
            Currency currencyPln = Currency.builder()
                    .code("PLN")
                    .name("Polski Złoty")
                    .crypto(false)
                    .build();
            currencyRepository.save(currencyPln);
        }
        if (!currencyRepository.existsByCode("USD")) {
            Currency currencyUsd = Currency.builder()
                    .code("USD")
                    .name("Dolar Amerykański")
                    .crypto(false)
                    .build();
            currencyRepository.save(currencyUsd);
        }
    }

}
