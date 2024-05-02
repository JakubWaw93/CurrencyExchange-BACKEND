package com.kodilla.currencyexchange.mapper;

import com.kodilla.currencyexchange.client.BinanceExchangeRateResponse;
import com.kodilla.currencyexchange.client.NbpExchangeRateResponse;
import com.kodilla.currencyexchange.client.NbpRate;
import com.kodilla.currencyexchange.domain.Currency;
import com.kodilla.currencyexchange.domain.ExchangeRate;
import com.kodilla.currencyexchange.domain.ExchangeRateDto;
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
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class ExchangeRateMapperTestSuite {

    @Autowired
    private ExchangeRateMapper exchangeRateMapper;
    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    private Currency currencyPln;
    private Currency currencyUsd;
    private Currency currencyBtc;
    private ExchangeRate exchangeRateUsdToPln;

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

        currencyRepository.save(currencyBtc);

        exchangeRateUsdToPln = ExchangeRate.builder()
                .rate(new BigDecimal("4.5"))
                .baseCurrency(currencyUsd)
                .targetCurrency(currencyPln)
                .lastUpdateTime(LocalDateTime.now())
                .build();
        exchangeRateRepository.save(exchangeRateUsdToPln);
    }

    @AfterEach
    void cleanUp() {
        currencyRepository.deleteAll();
    }

    @Test
    void testMapNBPResponseToExchangeRate() throws CurrencyNotFoundException {
        //Given
        NbpRate nbpRate = new NbpRate("085/A/NBP/2024", LocalDate.of(
                2024, 4, 30), new BigDecimal("4.0341"));
        NbpExchangeRateResponse nbpExchangeRateResponse = new NbpExchangeRateResponse(
                "a", "dolar amerykański", "USD", List.of(nbpRate));
        //When
        ExchangeRate exchangeRate = exchangeRateMapper.mapNbpResponseToExchangeRate(nbpExchangeRateResponse);
        //Then
        assertEquals("USD", exchangeRate.getBaseCurrency().getCode());
        assertEquals(new BigDecimal("4.0341"), exchangeRate.getRate());
        assertEquals(LocalDateTime.of(2024, 4, 30, 12, 0), exchangeRate.getLastUpdateTime());
    }

    @Test
    void testMapBinanceResponseToExchangeRate() throws CurrencyNotFoundException, ExchangeRateNotFoundException {
        //Given
        BinanceExchangeRateResponse binanceExchangeRateResponse = new BinanceExchangeRateResponse();
        binanceExchangeRateResponse.setPrice("60000.599");
        //When
        LocalDateTime localDateTime = LocalDateTime.now();
        ExchangeRate exchangeRate = exchangeRateMapper.mapBinanceResponseToExchangeRate(binanceExchangeRateResponse, currencyBtc.getCode());
        //Then
        assertEquals("BTC", exchangeRate.getBaseCurrency().getCode());
        assertEquals(new BigDecimal("60000.599").multiply(new BigDecimal("4.5")).setScale(4, RoundingMode.HALF_UP),
                exchangeRate.getRate().setScale(4, RoundingMode.HALF_UP));
        assertEquals("PLN", exchangeRate.getTargetCurrency().getCode());
        assertEquals(localDateTime.truncatedTo(ChronoUnit.SECONDS), exchangeRate.getLastUpdateTime().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    void testMapToExchangeRate() throws CurrencyNotFoundException {
        //Given
        ExchangeRateDto exchangeRateDto = ExchangeRateDto.builder()
                .id(12L)
                .rate(new BigDecimal("0.22"))
                .lastUpdateTime(LocalDateTime.now())
                .baseCurrencyId(currencyPln.getId())
                .targetCurrencyId(currencyUsd.getId())
                .build();
        //When
        ExchangeRate exchangeRate = exchangeRateMapper.mapToExchangeRate(exchangeRateDto);
        //Then
        assertEquals(new BigDecimal("0.22"), exchangeRate.getRate());
        assertEquals(12L, exchangeRate.getId());
        assertEquals(currencyPln.getCode(), exchangeRate.getBaseCurrency().getCode());
        assertEquals(currencyPln.getName(), exchangeRate.getBaseCurrency().getName());
    }

    @Test
    void testMapToExchangeRateDto() {
        //Given
        ExchangeRate exchangeRate = ExchangeRate.builder()
                .id(12L)
                .rate(new BigDecimal("0.22"))
                .lastUpdateTime(LocalDateTime.now())
                .baseCurrency(currencyPln)
                .targetCurrency(currencyUsd)
                .build();
        //When
        ExchangeRateDto exchangeRateDto = exchangeRateMapper.mapToExchangeRateDto(exchangeRate);
        //Then
        assertEquals(new BigDecimal("0.22"), exchangeRateDto.getRate());
        assertEquals(12L, exchangeRateDto.getId());
        assertEquals(currencyPln.getId(), exchangeRateDto.getBaseCurrencyId());
        assertEquals(currencyUsd.getId(), exchangeRateDto.getTargetCurrencyId());
    }

    @Test
    void testMapToExchangeRateDtoList() {
        //Given
        ExchangeRate exchangeRatePlnToUsd = ExchangeRate.builder()
                .id(12L)
                .rate(new BigDecimal("0.22"))
                .lastUpdateTime(LocalDateTime.now())
                .baseCurrency(currencyPln)
                .targetCurrency(currencyUsd)
                .build();
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        exchangeRates.add(exchangeRatePlnToUsd);
        exchangeRates.add(exchangeRateUsdToPln);
        //When
        List<ExchangeRateDto> exchangeRateDtoList = exchangeRateMapper.mapToExchangeRateDtoList(exchangeRates);
        //Then
        assertEquals(2, exchangeRateDtoList.size());
        assertEquals(exchangeRatePlnToUsd.getId(), exchangeRateDtoList.get(0).getId());
        assertEquals(exchangeRateUsdToPln.getId(), exchangeRateDtoList.get(1).getId());
    }
}
