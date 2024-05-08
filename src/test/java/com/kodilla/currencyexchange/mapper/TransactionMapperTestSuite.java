package com.kodilla.currencyexchange.mapper;

import com.kodilla.currencyexchange.domain.*;
import com.kodilla.currencyexchange.exception.CurrencyNotFoundException;
import com.kodilla.currencyexchange.exception.ExchangeRateNotFoundException;
import com.kodilla.currencyexchange.exception.UserNotFoundException;
import com.kodilla.currencyexchange.repository.CurrencyRepository;
import com.kodilla.currencyexchange.repository.ExchangeRateRepository;
import com.kodilla.currencyexchange.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class TransactionMapperTestSuite {

    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    private User user;
    private Currency currencyAaa;
    private Currency currencyBbb;
    private ExchangeRate exchangeRateBbbtoAaa;
    private LocalDateTime transactionDate = LocalDateTime.of(2024, 4, 30, 12,15);

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

        exchangeRateBbbtoAaa = ExchangeRate.builder()
                .rate(new BigDecimal("4.5"))
                .baseCurrency(currencyBbb)
                .targetCurrency(currencyAaa)
                .lastUpdateTime(LocalDateTime.now())
                .build();
        exchangeRateRepository.save(exchangeRateBbbtoAaa);

        user = User.builder()
                .apiKey("123456789")
                .emailAddress("something@gmail.com")
                .firstname("Jan")
                .lastname("Kowalski")
                .login("JanKow")
                .password("1111")
                .build();
        userRepository.save(user);
    }

    @AfterEach
    void cleanUp() {
        userRepository.deleteAll();
        exchangeRateRepository.deleteAll();
        currencyRepository.deleteAll();
    }

    @Test
    void testMapToTransaction() throws UserNotFoundException, ExchangeRateNotFoundException, CurrencyNotFoundException {
        //Given
        TransactionDto transactionDto = TransactionDto.builder()
                .id(111L)
                .status(TransactionStatus.SUCCEED.name())
                .userId(user.getId())
                .transactionDate(transactionDate)
                .soldCurrencyId(currencyBbb.getId())
                .boughtCurrencyId(currencyAaa.getId())
                .exchangeRateId(exchangeRateBbbtoAaa.getId())
                .amountBoughtCurrency(new BigDecimal("120000"))
                .build();
        //When
        Transaction transaction = transactionMapper.mapToTransaction(transactionDto);
        //Then
        assertEquals(111L, transaction.getId());
        assertEquals("SUCCEED", transaction.getStatus().name());
        assertEquals(transactionDate, transaction.getTransactionDate());
        assertEquals(currencyAaa.getId(), transaction.getBoughtCurrency().getId());
        assertEquals(currencyBbb.getId(), transaction.getSoldCurrency().getId());
        assertEquals(exchangeRateBbbtoAaa.getId(), transaction.getExchangeRate().getId());
        assertEquals(new BigDecimal("120000"), transaction.getAmountBoughtCurrency());
    }

    @Test
    void testMapToTransactionDto() {
        //Given
        Transaction transaction = Transaction.builder()
                .id(111L)
                .status(TransactionStatus.SUCCEED)
                .user(user)
                .transactionDate(transactionDate)
                .soldCurrency(currencyBbb)
                .boughtCurrency(currencyAaa)
                .exchangeRate(exchangeRateBbbtoAaa)
                .amountBoughtCurrency(new BigDecimal("120000"))
                .build();
        //When
        TransactionDto transactionDto = transactionMapper.mapToTransactionDto(transaction);
        //Then
        assertEquals(111L, transactionDto.getId());
        assertEquals("SUCCEED", transactionDto.getStatus());
        assertEquals(transactionDate, transactionDto.getTransactionDate());
        assertEquals(currencyAaa.getId(), transactionDto.getBoughtCurrencyId());
        assertEquals(currencyBbb.getId(), transactionDto.getSoldCurrencyId());
        assertEquals(exchangeRateBbbtoAaa.getId(), transactionDto.getExchangeRateId());
        assertEquals(new BigDecimal("120000"), transactionDto.getAmountBoughtCurrency());
    }

    @Test
    void testMapToTransactionDtoList() {
        //Given
        Transaction transaction1 = Transaction.builder()
                .id(111L)
                .status(TransactionStatus.SUCCEED)
                .user(user)
                .transactionDate(transactionDate)
                .soldCurrency(currencyBbb)
                .boughtCurrency(currencyAaa)
                .exchangeRate(exchangeRateBbbtoAaa)
                .amountBoughtCurrency(new BigDecimal("120000"))
                .build();

        Transaction transaction2 = Transaction.builder()
                .id(86L)
                .status(TransactionStatus.FAILED)
                .user(user)
                .transactionDate(transactionDate.minusHours(2))
                .soldCurrency(currencyBbb)
                .boughtCurrency(currencyAaa)
                .exchangeRate(exchangeRateBbbtoAaa)
                .amountBoughtCurrency(new BigDecimal("120000"))
                .build();

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);

        //When
        List<TransactionDto> transactionDtoList = transactionMapper.mapToTransactionDtoList(transactions);

        //Then
        assertEquals(2, transactionDtoList.size());
        assertEquals(86, transactionDtoList.get(1).getId());
        assertEquals("FAILED", transactionDtoList.get(1).getStatus());
        assertEquals(transactionDate.minusHours(2), transactionDtoList.get(1).getTransactionDate());
        assertEquals(currencyAaa.getId(), transactionDtoList.get(1).getBoughtCurrencyId());
        assertEquals(currencyBbb.getId(), transactionDtoList.get(1).getSoldCurrencyId());
        assertEquals(exchangeRateBbbtoAaa.getId(), transactionDtoList.get(1).getExchangeRateId());
        assertEquals(new BigDecimal("120000"), transactionDtoList.get(1).getAmountBoughtCurrency());
    }
}
