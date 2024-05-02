package com.kodilla.currencyexchange.domain;

import com.kodilla.currencyexchange.repository.CurrencyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
public class CurrencyTestSuite {

    @Autowired
    private CurrencyRepository currencyRepository;

    private Currency currency1;
    private Currency currency2;

    @BeforeEach
    public void createCurrency() {
        currency1 = Currency.builder()
                .code("PLN")
                .name("Zloty Polski")
                .crypto(false)
                .build();
        currency2 = Currency.builder()
                .code("USD")
                .name("American Dollar")
                .crypto(false)
                .active(false)
                .build();
    }

    @BeforeEach
    public void cleanUpBefore() {
        currencyRepository.deleteAll();
    }
    @AfterEach
    public void cleanUp() {
        currencyRepository.deleteAll();
    }

    @Test
    void testSaveAndGetByIdCurrency() {
        //Given
        //When
        currencyRepository.save(currency1);
        Optional<Currency> retrievedCurrency = currencyRepository.findByIdAndActiveTrue(currency1.getId());
        //Then
        assertTrue(retrievedCurrency.isPresent());
        assertNotNull(retrievedCurrency.get().getId());
        assertTrue(retrievedCurrency.get().isActive());
        assertFalse(retrievedCurrency.get().isCrypto());
        assertEquals("PLN", retrievedCurrency.get().getCode());
        assertTrue(currencyRepository.existsByCode("PLN"));
        assertFalse(currencyRepository.existsByCode("ETH"));
    }

    @Test
    void testGetListOfActiveCurrencies() {
        //Given
        currencyRepository.save(currency1);
        currencyRepository.save(currency2);
        //When
        List<Currency> currencies = currencyRepository.findAllByActiveTrue();
        //Then
        assertEquals(1, currencies.size());
        assertEquals(0, currencies.get(0).getBaseExchangeRates().size());

    }

    @Test
    void findActiveCurrencyByCodeAndDelete() {
        //Given
        currencyRepository.save(currency1);
        //When
        currencyRepository.deleteById(currency1.getId());
        Optional<Currency> currency = currencyRepository.findByCodeAndActiveTrue("PLN");
        //Then
        assertFalse(currency.isPresent());
    }
}
