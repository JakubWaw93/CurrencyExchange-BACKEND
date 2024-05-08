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

    private Currency currencyAaa;
    private Currency currencyBbb;
    private Currency currencyCcc;
    private ExchangeRate exchangeRateBbbToAaa;
    private ExchangeRate exchangeRateCcctoAaa;

    @AfterEach
    void cleanUp() {
        currencyRepository.deleteAll();
        exchangeRateRepository.deleteAll();
    }

    @BeforeEach
    void setEnvironment() {
        currencyAaa = Currency.builder()
                .code("AAA")
                .name("Waluta AAA")
                .crypto(false)
                .build();
        currencyBbb = Currency.builder()
                .code("BBB")
                .name("Waluta BBB")
                .crypto(false)
                .build();
        currencyCcc = Currency.builder()
                .code("CCC")
                .name("Waluta CCC - crypto")
                .crypto(true)
                .build();

        exchangeRateBbbToAaa = ExchangeRate.builder()
                .rate(new BigDecimal("4.5"))
                .baseCurrency(currencyBbb)
                .targetCurrency(currencyAaa)
                .lastUpdateTime(LocalDateTime.now())
                .build();
        exchangeRateRepository.save(exchangeRateBbbToAaa);

        exchangeRateCcctoAaa = ExchangeRate.builder()
                .rate(new BigDecimal("246000"))
                .baseCurrency(currencyCcc)
                .targetCurrency(currencyAaa)
                .lastUpdateTime(LocalDateTime.now())
                .build();
        exchangeRateRepository.save(exchangeRateCcctoAaa);

    }

    @Test
    void testCalculateRates() throws CurrencyNotFoundException, ExchangeRateNotFoundException {
        //Given
        String baseCurrencyCode = "CCC";
        String targetCurrencyCode = "BBB";
        //When
        exchangeRateService.calculateAndSaveMissingExchangeRates();
        //Then
        assertNotNull(exchangeRateRepository.findByBaseCurrencyCodeAndTargetCurrencyCode(baseCurrencyCode, targetCurrencyCode));

    }
}
