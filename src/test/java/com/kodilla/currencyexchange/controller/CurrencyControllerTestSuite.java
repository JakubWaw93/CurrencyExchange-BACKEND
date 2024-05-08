package com.kodilla.currencyexchange.controller;

import com.google.gson.Gson;
import com.kodilla.currencyexchange.domain.Currency;
import com.kodilla.currencyexchange.domain.CurrencyDto;
import com.kodilla.currencyexchange.mapper.CurrencyMapper;
import com.kodilla.currencyexchange.service.CurrencyService;
import org.hamcrest.Matchers;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(CurrencyController.class)
@EntityScan("com.kodilla.currencyexchange.domain")
@ContextConfiguration(classes = {CurrencyController.class, CurrencyMapper.class})
public class CurrencyControllerTestSuite {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CurrencyService currencyService;
    @MockBean
    private CurrencyMapper currencyMapper;

    @Test
    void shouldGetCurrencies() throws Exception {
        //Given
        List<Currency> currencies = List.of(Currency.builder()
                .id(1L)
                .code("AAA")
                .name("Waluta AAA")
                .crypto(false)
                .build());
        List<CurrencyDto> currenciesDto = List.of(CurrencyDto.builder()
                .id(1L)
                .code("AAA")
                .name("Waluta AAA")
                .crypto(false)
                .build());
        when(currencyService.getAllCurrencies()).thenReturn(currencies);
        when(currencyMapper.mapToCurrencyDtoList(currencies)).thenReturn(currenciesDto);
        //When&Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/currencies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].code", Matchers.is("AAA")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("Waluta AAA")));
    }

    @Test
    void shouldGetCurrency() throws Exception {
        //Given
        Currency currency = Currency.builder()
                .id(1L)
                .code("AAA")
                .name("Waluta AAA")
                .crypto(false)
                .build();
        CurrencyDto currencyDto = CurrencyDto.builder()
                .id(1L)
                .code("AAA")
                .name("Waluta AAA")
                .crypto(false)
                .build();
        when(currencyService.getCurrencyByCode(any())).thenReturn(currency);
        when(currencyService.getCurrencyById(any())).thenReturn(currency);
        when(currencyMapper.mapToCurrencyDto(currency)).thenReturn(currencyDto);
        //When&Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/currencies/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("AAA")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Waluta AAA")));

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/currencies/code/AAA")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("AAA")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Waluta AAA")));
    }

    @Test
    void shouldCreateCurrency() throws Exception {
        //Given
        CurrencyDto currencyDto = CurrencyDto.builder()
                .id(1L)
                .code("AAA")
                .name("Waluta AAA")
                .crypto(false)
                .build();
        Gson gson = new Gson();
        String jsonContent = gson.toJson(currencyDto);
        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/currencies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void shouldUpdateCurrency() throws Exception {
        //Given
        Currency currency = Currency.builder()
                .id(1L)
                .code("AAA")
                .name("Waluta AAA")
                .crypto(false)
                .build();
        CurrencyDto currencyDto = CurrencyDto.builder()
                .id(1L)
                .code("AAA")
                .name("Waluta AAA")
                .crypto(false)
                .build();

        when(currencyMapper.mapToCurrency(any(CurrencyDto.class))).thenReturn(currency);
        when(currencyMapper.mapToCurrencyDto(any(Currency.class))).thenReturn(currencyDto);
        when(currencyService.getCurrencyById(1L)).thenReturn(currency);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(currencyDto);
        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/currencies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("AAA")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Waluta AAA")));
    }

    @Test
    void shouldDeleteCurrency() throws Exception {
        // Given
        long currencyId = 1;
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/currencies/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
