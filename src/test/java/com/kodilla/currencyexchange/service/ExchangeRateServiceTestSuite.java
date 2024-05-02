package com.kodilla.currencyexchange.service;

import com.kodilla.currencyexchange.domain.Currency;
import com.kodilla.currencyexchange.domain.ExchangeRate;
import com.kodilla.currencyexchange.exception.CurrencyNotFoundException;
import com.kodilla.currencyexchange.exception.ExchangeRateNotFoundException;
import com.kodilla.currencyexchange.repository.CurrencyRepository;
import com.kodilla.currencyexchange.repository.ExchangeRateRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SpringBootTest
@Transactional
public class ExchangeRateServiceTestSuite {


    @Autowired
    private ExchangeRateService exchangeRateService;
    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    private Currency currencyPln;
    private Currency currencyUsd;
    private Currency currencyBtc;
    private ExchangeRate exchangeRateUsdToPln;
    private ExchangeRate exchangeRateBtcToPln;

    @AfterEach
    void cleanUp() {
        currencyRepository.deleteAll();
        exchangeRateRepository.deleteAll();
    }

    @BeforeEach
    void setEnvironment() {
        currencyPln = Currency.builder()
                .code("PLN")
                .name("Zloty Polski")
                .crypto(false)
                .build();
        currencyUsd = Currency.builder()
                .code("USD")
                .name("American Dollar")
                .crypto(false)
                .build();
        currencyBtc = Currency.builder()
                .code("BTC")
                .name("Bitcoin")
                .crypto(true)
                .build();

        exchangeRateUsdToPln = ExchangeRate.builder()
                .rate(new BigDecimal("4.5"))
                .baseCurrency(currencyUsd)
                .targetCurrency(currencyPln)
                .lastUpdateTime(LocalDateTime.now())
                .build();
        exchangeRateRepository.save(exchangeRateUsdToPln);

        exchangeRateBtcToPln = ExchangeRate.builder()
                .rate(new BigDecimal("246000"))
                .baseCurrency(currencyBtc)
                .targetCurrency(currencyPln)
                .lastUpdateTime(LocalDateTime.now())
                .build();
        exchangeRateRepository.save(exchangeRateBtcToPln);

    }

    @Test
    void testCalculateRates() throws CurrencyNotFoundException, ExchangeRateNotFoundException {
        //Given
        String baseCurrencyCode = "BTC";
        String targetCurrencyCode = "USD";
        //When
        exchangeRateService.calculateAndSaveMissingExchangeRates();
        //Then
        assertNotNull(exchangeRateRepository.findByBaseCurrencyCodeAndTargetCurrencyCode(baseCurrencyCode, targetCurrencyCode));
        System.out.println(exchangeRateRepository.findByBaseCurrencyCodeAndTargetCurrencyCode(baseCurrencyCode, targetCurrencyCode).get().getRate());



    }
}
