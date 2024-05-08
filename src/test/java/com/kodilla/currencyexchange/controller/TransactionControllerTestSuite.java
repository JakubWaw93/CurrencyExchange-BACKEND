package com.kodilla.currencyexchange.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kodilla.currencyexchange.configuration.LocalDateTimeAdapter;
import com.kodilla.currencyexchange.domain.*;
import com.kodilla.currencyexchange.mapper.CurrencyMapper;
import com.kodilla.currencyexchange.mapper.TransactionMapper;
import com.kodilla.currencyexchange.service.TransactionService;
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
@WebMvcTest(TransactionController.class)
@EntityScan("com.kodilla.currencyexchange.domain")
@ContextConfiguration(classes = {TransactionController.class, TransactionMapper.class})
public class TransactionControllerTestSuite {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TransactionService transactionService;
    @MockBean
    private TransactionMapper transactionMapper;

    private User user;
    private Currency boughtCurrency;
    private Currency soldCurrency;
    private ExchangeRate exchangeRate;

    @BeforeEach
    void addCurrenciesExchangeRateAndUser() {
        user = User.builder()
                .id(1L)
                .firstname("Jan")
                .lastname("Nowak")
                .login("JaNow")
                .emailAddress("JanNowak@gmail.com")
                .password("password")
                .build();
        boughtCurrency = Currency.builder()
                .id(1L)
                .code("AAA")
                .name("Waluta AAA")
                .crypto(false)
                .build();
        soldCurrency = Currency.builder()
                .id(2L)
                .code("BBB")
                .name("Waluta BBB")
                .crypto(false)
                .build();
        exchangeRate = ExchangeRate.builder()
                .rate(new BigDecimal(5))
                .lastUpdateTime(LocalDateTime.now())
                .baseCurrency(boughtCurrency)
                .targetCurrency(soldCurrency)
                .id(1L)
                .build();
    }

    @Test
    void shouldGetTransactions() throws Exception {
        //Given
        List<Transaction> transactions = List.of(Transaction.builder()
                .id(1L)
                .amountBoughtCurrency(new BigDecimal(1000))
                .transactionDate(LocalDateTime.of(2000, 5, 12, 15, 23))
                .soldCurrency(soldCurrency)
                .boughtCurrency(boughtCurrency)
                .exchangeRate(exchangeRate)
                .user(user)
                .status(TransactionStatus.IN_PROGRESS)
                .build());
        List<TransactionDto> transactionDtos = List.of(TransactionDto.builder()
                .id(1L)
                .amountBoughtCurrency(new BigDecimal(1000))
                .transactionDate(LocalDateTime.of(2000, 5, 12, 15, 23))
                .soldCurrencyId(soldCurrency.getId())
                .boughtCurrencyId(boughtCurrency.getId())
                .exchangeRateId(exchangeRate.getId())
                .userId(user.getId())
                .status(TransactionStatus.IN_PROGRESS.toString())
                .build());
        when(transactionService.getAllTransaction()).thenReturn(transactions);
        when(transactionMapper.mapToTransactionDtoList(transactions)).thenReturn(transactionDtos);
        //When&Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/transactions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].amountBoughtCurrency", Matchers.is(1000)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].soldCurrencyId", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].boughtCurrencyId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status", Matchers.is("IN_PROGRESS")));
    }

    @Test
    void shouldGetTransactionsByUserId() throws Exception {
        //Given
        List<Transaction> transactions = List.of(Transaction.builder()
                .id(1L)
                .amountBoughtCurrency(new BigDecimal(1000))
                .transactionDate(LocalDateTime.of(2000, 5, 12, 15, 23))
                .soldCurrency(soldCurrency)
                .boughtCurrency(boughtCurrency)
                .exchangeRate(exchangeRate)
                .user(user)
                .status(TransactionStatus.IN_PROGRESS)
                .build());
        List<TransactionDto> transactionDtos = List.of(TransactionDto.builder()
                .id(1L)
                .amountBoughtCurrency(new BigDecimal(1000))
                .transactionDate(LocalDateTime.of(2000, 5, 12, 15, 23))
                .soldCurrencyId(soldCurrency.getId())
                .boughtCurrencyId(boughtCurrency.getId())
                .exchangeRateId(exchangeRate.getId())
                .userId(user.getId())
                .status(TransactionStatus.IN_PROGRESS.toString())
                .build());
        when(transactionService.getAllTransactionsByUserId(1L)).thenReturn(transactions);
        when(transactionMapper.mapToTransactionDtoList(transactions)).thenReturn(transactionDtos);
        //When&Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/transactions/userid/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].amountBoughtCurrency", Matchers.is(1000)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].soldCurrencyId", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].boughtCurrencyId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status", Matchers.is("IN_PROGRESS")));
    }

    @Test
    void shouldGetTransactionsByUserLogin() throws Exception {
        //Given
        List<Transaction> transactions = List.of(Transaction.builder()
                .id(1L)
                .amountBoughtCurrency(new BigDecimal(1000))
                .transactionDate(LocalDateTime.of(2000, 5, 12, 15, 23))
                .soldCurrency(soldCurrency)
                .boughtCurrency(boughtCurrency)
                .exchangeRate(exchangeRate)
                .user(user)
                .status(TransactionStatus.IN_PROGRESS)
                .build());
        List<TransactionDto> transactionDtos = List.of(TransactionDto.builder()
                .id(1L)
                .amountBoughtCurrency(new BigDecimal(1000))
                .transactionDate(LocalDateTime.of(2000, 5, 12, 15, 23))
                .soldCurrencyId(soldCurrency.getId())
                .boughtCurrencyId(boughtCurrency.getId())
                .exchangeRateId(exchangeRate.getId())
                .userId(user.getId())
                .status(TransactionStatus.IN_PROGRESS.toString())
                .build());
        when(transactionService.getAllTransactionsByUserLogin("JaNow")).thenReturn(transactions);
        when(transactionMapper.mapToTransactionDtoList(transactions)).thenReturn(transactionDtos);
        //When&Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/transactions/userlogin/JaNow")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].amountBoughtCurrency", Matchers.is(1000)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].soldCurrencyId", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].boughtCurrencyId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].status", Matchers.is("IN_PROGRESS")));
    }

    @Test
    void shouldGetTransactionById() throws Exception {
        //Given
        Transaction transaction = Transaction.builder()
                .id(1L)
                .amountBoughtCurrency(new BigDecimal(1000))
                .transactionDate(LocalDateTime.of(2000, 5, 12, 15, 23))
                .soldCurrency(soldCurrency)
                .boughtCurrency(boughtCurrency)
                .exchangeRate(exchangeRate)
                .user(user)
                .status(TransactionStatus.IN_PROGRESS)
                .build();
        TransactionDto transactionDto = TransactionDto.builder()
                .id(1L)
                .amountBoughtCurrency(new BigDecimal(1000))
                .transactionDate(LocalDateTime.of(2000, 5, 12, 15, 23))
                .soldCurrencyId(soldCurrency.getId())
                .boughtCurrencyId(boughtCurrency.getId())
                .exchangeRateId(exchangeRate.getId())
                .userId(user.getId())
                .status(TransactionStatus.IN_PROGRESS.toString())
                .build();
        when(transactionService.getTransactionById(1L)).thenReturn(transaction);
        when(transactionMapper.mapToTransactionDto(transaction)).thenReturn(transactionDto);
        //When&Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/transactions/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amountBoughtCurrency", Matchers.is(1000)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.soldCurrencyId", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.boughtCurrencyId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is("IN_PROGRESS")));
    }

    @Test
    void shouldCreateTransaction() throws Exception {
        //Given
        TransactionDto transactionDto = TransactionDto.builder()
                .id(1L)
                .amountBoughtCurrency(new BigDecimal(1000))
                .transactionDate(LocalDateTime.of(2000, 5, 12, 15, 23))
                .soldCurrencyId(soldCurrency.getId())
                .boughtCurrencyId(boughtCurrency.getId())
                .exchangeRateId(exchangeRate.getId())
                .userId(user.getId())
                .status(TransactionStatus.IN_PROGRESS.toString())
                .build();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        String jsonContent = gson.toJson(transactionDto);
        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void shouldUpdateCurrency() throws Exception {
        //Given
        Transaction transaction = Transaction.builder()
                .id(1L)
                .amountBoughtCurrency(new BigDecimal(1000))
                .transactionDate(LocalDateTime.of(2000, 5, 12, 15, 23))
                .soldCurrency(soldCurrency)
                .boughtCurrency(boughtCurrency)
                .exchangeRate(exchangeRate)
                .user(user)
                .status(TransactionStatus.IN_PROGRESS)
                .build();
        TransactionDto transactionDto = TransactionDto.builder()
                .id(1L)
                .amountBoughtCurrency(new BigDecimal(1000))
                .transactionDate(LocalDateTime.of(2000, 5, 12, 15, 23))
                .soldCurrencyId(soldCurrency.getId())
                .boughtCurrencyId(boughtCurrency.getId())
                .exchangeRateId(exchangeRate.getId())
                .userId(user.getId())
                .status(TransactionStatus.IN_PROGRESS.toString())
                .build();

        when(transactionMapper.mapToTransaction(any(TransactionDto.class))).thenReturn(transaction);
        when(transactionMapper.mapToTransactionDto(any(Transaction.class))).thenReturn(transactionDto);
        when(transactionService.getTransactionById(1L)).thenReturn(transaction);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        String jsonContent = gson.toJson(transaction);
        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.amountBoughtCurrency", Matchers.is(1000)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.soldCurrencyId", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.boughtCurrencyId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", Matchers.is("IN_PROGRESS")));
    }

}
