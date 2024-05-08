package com.kodilla.currencyexchange.client;

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

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class BinanceClientTestSuite {

    @Autowired
    private BinanceClient binanceClient;
    @Autowired
    private ExchangeRateRepository exchangeRateRepository;
    @Autowired
    private CurrencyRepository currencyRepository;

    @AfterEach
    void cleanUp() {
        exchangeRateRepository.deleteAll();
        currencyRepository.deleteAll();
    }

    @BeforeEach
    @Transactional void addCurrencyAndExchangeRate() throws CurrencyNotFoundException {

        Currency currencyBtc = Currency.builder()
                .code("BTC")
                .name("Bitcoin")
                .crypto(true)
                .build();
        currencyRepository.save(currencyBtc);

        ExchangeRate exchangeRateUsdToPln = ExchangeRate.builder()
                .rate(new BigDecimal("4.0845"))
                .baseCurrency(currencyRepository.findByCodeAndActiveTrue("USD").orElseThrow(CurrencyNotFoundException::new))
                .targetCurrency(currencyRepository.findByCodeAndActiveTrue("PLN").orElseThrow(CurrencyNotFoundException::new))
                .lastUpdateTime(LocalDateTime.now())
                .build();
        exchangeRateRepository.save(exchangeRateUsdToPln);
    }


    @Test
    void testGetExchangeRateFromBinanceApi() {
        //Given
        //When
        ExchangeRate exchangeRateEthPln = binanceClient.getExchangeRateFromBinanceApi("ETH");
        //Then
        assertNotNull(exchangeRateRepository.findByBaseCurrencyCodeAndTargetCurrencyCode("ETH", "PLN"));
        assertEquals("ETH", exchangeRateEthPln.getBaseCurrency().getCode());
        assertNotNull(exchangeRateEthPln.getRate());
        assertEquals("ETH name update needed.", exchangeRateEthPln.getBaseCurrency().getName());
        System.out.println(exchangeRateEthPln.getRate());
    }

    @Test
    void testUpdateExchangeRates() throws ExchangeRateNotFoundException {
        //Given
        //When
        binanceClient.updateExchangeRates();
        //Then
        ExchangeRate exchangeRate = exchangeRateRepository.findByBaseCurrencyCodeAndTargetCurrencyCode("BTC", "PLN").orElseThrow(ExchangeRateNotFoundException::new);
        assertNotNull(exchangeRate);


    }
}
