package com.kodilla.currencyexchange.client;

import com.kodilla.currencyexchange.domain.Currency;
import com.kodilla.currencyexchange.domain.ExchangeRate;
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
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class NbpClientTestSuite {

    @Autowired
    private NbpClient nbpClient;
    @Autowired
    private ExchangeRateRepository exchangeRateRepository;
    @Autowired
    private CurrencyRepository currencyRepository;

    @AfterEach
    void cleanUp() {
        exchangeRateRepository.deleteAll();
        currencyRepository.deleteAll();
    }


    @Test
    void testGetExchangeRateFromNbpApi() {
        //Given
        Currency currencyRub = Currency.builder()
                .code("RUB")
                .name("Rubel Rosyjski")
                .crypto(false)
                .build();
        currencyRepository.save(currencyRub);
        //When
        ExchangeRate exchangeRate = nbpClient.getExchangeRateFromNbpApi("a", "RUB");
        //Then
        assertNotNull(exchangeRate);
        assertEquals("RUB", exchangeRate.getBaseCurrency().getCode());
    }

    @Test
    void testLookForExchangeRateFromBinanceApi() {
        //Given
        List<String> tables = Arrays.asList("a", "b", "c");
        //When
        ExchangeRate exchangeRate = nbpClient.lookForExchangeRateFromNbpApi(tables, "RUB");
        //Then
        assertNotNull(exchangeRate);
        System.out.println(exchangeRate.getRate());
    }

    @Test
    void testUpdateExchangeRates() throws ExchangeRateNotFoundException {
        //Given
        Currency currencyRub = Currency.builder()
                .code("RUB")
                .name("Rubel Rosyjski")
                .crypto(false)
                .build();
        currencyRepository.save(currencyRub);
        //When
        nbpClient.updateExchangeRates();
        //Then
        ExchangeRate exchangeRate = exchangeRateRepository.findByBaseCurrencyCodeAndTargetCurrencyCode("RUB", "PLN").orElseThrow(ExchangeRateNotFoundException::new);
        assertNotNull(exchangeRate);
        System.out.println(exchangeRate.getRate());;

    }
}
