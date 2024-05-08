package com.kodilla.currencyexchange.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kodilla.currencyexchange.configuration.LocalDateTimeAdapter;
import com.kodilla.currencyexchange.domain.Currency;
import com.kodilla.currencyexchange.domain.ExchangeRate;
import com.kodilla.currencyexchange.domain.ExchangeRateDto;
import com.kodilla.currencyexchange.mapper.ExchangeRateMapper;
import com.kodilla.currencyexchange.service.ExchangeRateService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(ExchangeRateController.class)
@EntityScan("com.kodilla.currencyexchange.domain")
@ContextConfiguration(classes = {ExchangeRateController.class, ExchangeRateMapper.class})
public class ExchangeRateControllerTestSuite {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ExchangeRateService exchangeRateService;
    @MockBean
    private ExchangeRateMapper exchangeRateMapper;

    private Currency baseCurrency;
    private Currency targetCurrency;

    @BeforeEach
    void addCurrencies() {
        baseCurrency = Currency.builder()
                .id(1L)
                .code("AAA")
                .name("Waluta AAA")
                .crypto(false)
                .build();
        targetCurrency = Currency.builder()
                .id(2L)
                .code("BBB")
                .name("Waluta BBB")
                .crypto(false)
                .build();
    }

    @Test
    void shouldGetAllExchangeRates() throws Exception {
        //Given
        List<ExchangeRate> exchangeRates = List.of(ExchangeRate.builder()
                .id(1L)
                .baseCurrency(baseCurrency)
                .targetCurrency(targetCurrency)
                .rate(new BigDecimal(2))
                .lastUpdateTime(LocalDateTime.of(2024, 4, 15, 12, 0))
                .build());
        List<ExchangeRateDto> exchangeRateDtoList = List.of(ExchangeRateDto.builder()
                .id(1L)
                .baseCurrencyId(1L)
                .targetCurrencyId(2L)
                .rate(new BigDecimal(2))
                .lastUpdateTime(LocalDateTime.of(2024, 4, 15, 12, 0))
                .build());
        when(exchangeRateService.getAllExchangeRates()).thenReturn(exchangeRates);
        when(exchangeRateMapper.mapToExchangeRateDtoList(exchangeRates)).thenReturn(exchangeRateDtoList);
        //When&Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/exchangerates")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].baseCurrencyId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].targetCurrencyId", Matchers.is(2)));
    }

    @Test
    void shouldGetAllExchangeRatesByBaseCurrencyCode() throws Exception {
        //Given
        List<ExchangeRate> exchangeRates = List.of(ExchangeRate.builder()
                .id(1L)
                .baseCurrency(baseCurrency)
                .targetCurrency(targetCurrency)
                .rate(new BigDecimal(2))
                .lastUpdateTime(LocalDateTime.of(2024, 4, 15, 12, 0))
                .build());
        List<ExchangeRateDto> exchangeRateDtoList = List.of(ExchangeRateDto.builder()
                .id(1L)
                .baseCurrencyId(1L)
                .targetCurrencyId(2L)
                .rate(new BigDecimal(2))
                .lastUpdateTime(LocalDateTime.of(2024, 4, 15, 12, 0))
                .build());
        when(exchangeRateService.getExchangeRatesByBaseCurrencyCode("AAA")).thenReturn(exchangeRates);
        when(exchangeRateMapper.mapToExchangeRateDtoList(exchangeRates)).thenReturn(exchangeRateDtoList);
        //When&Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/exchangerates/code/base/AAA")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].baseCurrencyId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].targetCurrencyId", Matchers.is(2)));
    }

    @Test
    void shouldGetAllExchangeRatesByTargetCurrencyCode() throws Exception {
        //Given
        List<ExchangeRate> exchangeRates = List.of(ExchangeRate.builder()
                .id(1L)
                .baseCurrency(baseCurrency)
                .targetCurrency(targetCurrency)
                .rate(new BigDecimal(2))
                .lastUpdateTime(LocalDateTime.of(2024, 4, 15, 12, 0))
                .build());
        List<ExchangeRateDto> exchangeRateDtoList = List.of(ExchangeRateDto.builder()
                .id(1L)
                .baseCurrencyId(1L)
                .targetCurrencyId(2L)
                .rate(new BigDecimal(2))
                .lastUpdateTime(LocalDateTime.of(2024, 4, 15, 12, 0))
                .build());
        when(exchangeRateService.getExchangeRatesByTargetCurrencyCode("BBB")).thenReturn(exchangeRates);
        when(exchangeRateMapper.mapToExchangeRateDtoList(exchangeRates)).thenReturn(exchangeRateDtoList);
        //When&Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/exchangerates/code/target/BBB")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].baseCurrencyId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].targetCurrencyId", Matchers.is(2)));
    }

    @Test
    void shouldGetExchangeRateById() throws Exception {
        //Given
        ExchangeRate exchangeRate = ExchangeRate.builder()
                .id(1L)
                .baseCurrency(baseCurrency)
                .targetCurrency(targetCurrency)
                .rate(new BigDecimal(2))
                .lastUpdateTime(LocalDateTime.of(2024, 4, 15, 12, 0))
                .build();
        ExchangeRateDto exchangeRateDto = ExchangeRateDto.builder()
                .id(1L)
                .baseCurrencyId(1L)
                .targetCurrencyId(2L)
                .rate(new BigDecimal(2))
                .lastUpdateTime(LocalDateTime.of(2024, 4, 15, 12, 0))
                .build();
        when(exchangeRateService.getExchangeRateById(any())).thenReturn(exchangeRate);
        when(exchangeRateMapper.mapToExchangeRateDto(exchangeRate)).thenReturn(exchangeRateDto);
        //When&Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/exchangerates/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.baseCurrencyId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.targetCurrencyId", Matchers.is(2)));
    }

    @Test
    void shouldGetExchangeRateByCurrenciesCodes() throws Exception {
        //Given
        ExchangeRate exchangeRate = ExchangeRate.builder()
                .id(1L)
                .baseCurrency(baseCurrency)
                .targetCurrency(targetCurrency)
                .rate(new BigDecimal(2))
                .lastUpdateTime(LocalDateTime.of(2024, 4, 15, 12, 0))
                .build();
        ExchangeRateDto exchangeRateDto = ExchangeRateDto.builder()
                .id(1L)
                .baseCurrencyId(1L)
                .targetCurrencyId(2L)
                .rate(new BigDecimal(2))
                .lastUpdateTime(LocalDateTime.of(2024, 4, 15, 12, 0))
                .build();
        when(exchangeRateService.getExchangeRateByCurrencyCodes("AAA", "BBB")).thenReturn(exchangeRate);
        when(exchangeRateMapper.mapToExchangeRateDto(exchangeRate)).thenReturn(exchangeRateDto);
        //When&Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/exchangerates/codes/AAA/BBB")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.baseCurrencyId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.targetCurrencyId", Matchers.is(2)));
    }

    @Test
    void shouldCreateExchangeRate() throws Exception {
        //Given
        ExchangeRateDto exchangeRateDto = ExchangeRateDto.builder()
                .id(1L)
                .baseCurrencyId(1L)
                .targetCurrencyId(2L)
                .rate(new BigDecimal(2))
                .lastUpdateTime(LocalDateTime.of(2024, 4, 15, 12, 0,0))
                .build();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        String jsonContent = gson.toJson(exchangeRateDto);
        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/exchangerates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

}
