package com.kodilla.currencyexchange.mapper;

import com.kodilla.currencyexchange.domain.Currency;
import com.kodilla.currencyexchange.domain.CurrencyDto;
import com.kodilla.currencyexchange.repository.ExchangeRateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.cassandra.DataCassandraTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class CurrencyMapperTestSuite {

    @Autowired
    private CurrencyMapper currencyMapper;

    @Test
    void mapToCurrency() {
        //Given
        CurrencyDto currencyDto = CurrencyDto.builder()
                .id(1L)
                .code("PLN")
                .name("Polski Złoty")
                .crypto(false)
                .build();
        //When
        Currency currency = currencyMapper.mapToCurrency(currencyDto);
        //Then
        assertEquals(1L, currency.getId());
        assertEquals("PLN", currency.getCode());
        assertEquals("Polski Złoty", currency.getName());
        assertFalse(currency.isCrypto());
        assertTrue(currency.isActive());
        assertEquals(0, currency.getBaseExchangeRates().size());
        assertEquals(0, currency.getTargetExchangeRates().size());
        assertEquals(0, currency.getSoldInTransactions().size());
        assertEquals(0, currency.getBoughtInTransactions().size());
    }

    @Test
    void mapToCurrencyDto() {
        //Given
        Currency currency = Currency.builder()
                .id(1L)
                .code("PLN")
                .name("Polski Złoty")
                .crypto(false)
                .build();
        //When
        CurrencyDto currencyDto = currencyMapper.mapToCurrencyDto(currency);
        //Then
        assertEquals(1L, currency.getId());
        assertEquals("PLN", currency.getCode());
        assertEquals("Polski Złoty", currency.getName());
        assertFalse(currency.isCrypto());
        assertTrue(currency.isActive());
        assertEquals(0, currency.getBaseExchangeRates().size());
        assertEquals(0, currency.getTargetExchangeRates().size());
        assertEquals(0, currency.getSoldInTransactions().size());
        assertEquals(0, currency.getBoughtInTransactions().size());
    }

    @Test
    void mapToCurrencyDtoList() {
        //Given
        Currency currency1 = Currency.builder()
                .id(1L)
                .code("PLN")
                .name("Polski Złoty")
                .crypto(false)
                .build();
        Currency currency2 = Currency.builder()
                .id(2L)
                .code("USD")
                .name("American Dollar")
                .crypto(false)
                .build();
        List<Currency> currencies = new ArrayList<>();
        currencies.add(currency1);
        currencies.add(currency2);
        //When
        List<CurrencyDto> currencyDtoList = currencyMapper.mapToCurrencyDtoList(currencies);
        //Then
        assertEquals(2, currencyDtoList.size());
        assertEquals(1L, currencies.get(0).getId());
        assertEquals("PLN", currencies.get(0).getCode());
        assertEquals("Polski Złoty", currencies.get(0).getName());
        assertFalse(currencies.get(0).isCrypto());
        assertTrue(currencies.get(0).isActive());
        assertEquals(0, currencies.get(0).getBaseExchangeRates().size());
        assertEquals(0, currencies.get(0).getTargetExchangeRates().size());
        assertEquals(0, currencies.get(0).getSoldInTransactions().size());
        assertEquals(0, currencies.get(0).getBoughtInTransactions().size());
    }

}
