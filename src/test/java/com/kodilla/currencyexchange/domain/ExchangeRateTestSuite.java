package com.kodilla.currencyexchange.domain;

import com.kodilla.currencyexchange.repository.CurrencyRepository;
import com.kodilla.currencyexchange.repository.ExchangeRateRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class ExchangeRateTestSuite {

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;
    @Autowired
    private CurrencyRepository currencyRepository;

    private ExchangeRate exchangeRate;
    private LocalDateTime lastUpdate = LocalDateTime.of(LocalDate.of(2024,4,28), LocalTime.of(15,0));

    @BeforeEach
    void createExchangeRate() {

        Currency currency1 = Currency.builder()
                .code("PLN")
                .name("Zloty Polski")
                .crypto(false)
                .build();
        Currency currency2 = Currency.builder()
                .code("USD")
                .name("American Dollar")
                .crypto(false)
                .active(false)
                .build();

        exchangeRate = ExchangeRate.builder()
                .baseCurrency(currency1)
                .targetCurrency(currency2)
                .rate(new BigDecimal(5))
                .lastUpdateTime(LocalDateTime.of(LocalDate.of(2024,4,28), LocalTime.of(15,0)))
                .build();

    }

    @BeforeEach
    public void cleanUpBefore() {
        exchangeRateRepository.deleteAll();
        currencyRepository.deleteAll();
    }
    @AfterEach
    public void cleanUp() {
        exchangeRateRepository.deleteAll();
        currencyRepository.deleteAll();
    }

    @Test
    void testSaveExchangeRateAndFindById() {
        //Given
        //When
        exchangeRateRepository.save(exchangeRate);
        Optional<ExchangeRate> retrievedExchangeRate = exchangeRateRepository.findById(exchangeRate.getId());
        //Then
        assertTrue(retrievedExchangeRate.isPresent());
        assertEquals(new BigDecimal(5), retrievedExchangeRate.get().getRate());
    }

    @Test
    void testGetExchangeRateByCurrenciesCodes() {
        //Given
        //When
        exchangeRateRepository.save(exchangeRate);
        Optional<ExchangeRate> retrievedExchangeRate = exchangeRateRepository.findByBaseCurrencyCodeAndTargetCurrencyCode("PLN","USD");
        //Then
        assertTrue(retrievedExchangeRate.isPresent());
        assertEquals(lastUpdate, retrievedExchangeRate.get().getLastUpdateTime());
    }

    @Test
    void testGetAllSExchangeRates() {
        //Given
        exchangeRateRepository.save(exchangeRate);
        //When
        List<ExchangeRate> exchangeRates = exchangeRateRepository.findAll();
        //Then
        assertEquals(1, exchangeRates.size());
        assertEquals(exchangeRate.getId(), exchangeRates.get(0).getId());
    }

}
