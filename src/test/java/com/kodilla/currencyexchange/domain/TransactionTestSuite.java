package com.kodilla.currencyexchange.domain;

import com.kodilla.currencyexchange.repository.CurrencyRepository;
import com.kodilla.currencyexchange.repository.ExchangeRateRepository;
import com.kodilla.currencyexchange.repository.TransactionRepository;
import com.kodilla.currencyexchange.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
public class TransactionTestSuite {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private ExchangeRateRepository exchangeRateRepository;
    @Autowired
    private UserRepository userRepository;

    private Transaction transaction;
    private final LocalDateTime transactionDate = LocalDateTime.of(LocalDate.of(2024,4,28), LocalTime.of(15,0));

    @BeforeEach
    void createTransaction() {

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

        ExchangeRate exchangeRate = ExchangeRate.builder()
                .baseCurrency(currency1)
                .targetCurrency(currency2)
                .rate(new BigDecimal(5))
                .lastUpdateTime(LocalDateTime.of(LocalDate.of(2024,4,28), LocalTime.of(15,0)))
                .build();

        exchangeRateRepository.save(exchangeRate);

        User user = User.builder()
                .firstname("Jan")
                .lastname("Kowalski")
                .emailAddress("JanKowalski@gmail.com")
                .apiKey("24987463541165498436516854")
                .build();

        userRepository.save(user);

        transaction = Transaction.builder()
                .transactionDate(transactionDate)
                .exchangeRate(exchangeRate)
                .boughtCurrency(currency1)
                .soldCurrency(currency2)
                .amountBoughtCurrency(new BigDecimal(10))
                .user(user)
                .status(TransactionStatus.IN_PROGRESS)
                .build();

    }

    @AfterEach
    public void cleanUp() {
        transactionRepository.deleteAll();
        exchangeRateRepository.deleteAll();
        currencyRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testSaveTransactionAndFindById() {
        //Given
        //When
        transactionRepository.save(transaction);
        Optional<Transaction> retrievedTransaction = transactionRepository.findById(transaction.getId());
        //Then
        assertTrue(retrievedTransaction.isPresent());
        assertEquals(transactionDate, retrievedTransaction.get().getTransactionDate());
    }

    @Test
    void testGetAllTransactions() {
        //Given
        transactionRepository.save(transaction);
        //When
        List<Transaction> transactions = transactionRepository.findAll();
        //Then
        assertEquals(1, transactions.size());
        assertEquals("USD", transactions.get(0).getExchangeRate().getTargetCurrency().getCode());
    }

    @Test
    void testGetAllTransactionsByUser() {
        //Given
        transactionRepository.save(transaction);
        //When
        List<Transaction> transactions = transactionRepository.findAllByUserId(transaction.getUser().getId());
        //Then
        assertEquals(1, transactions.size());
        assertEquals(new BigDecimal(10), transactions.get(0).getAmountBoughtCurrency());
    }

    @Test
    void getAllTransactionByStatus() {
        //Given
        transactionRepository.save(transaction);
        //When
        List<Transaction> inProgressTransactions = transactionRepository.findAllByStatusIs(TransactionStatus.IN_PROGRESS);
        List<Transaction> failedTransactions = transactionRepository.findAllByStatusIs(TransactionStatus.FAILED);
        //Then
        assertEquals(1, inProgressTransactions.size());
        assertEquals(0, failedTransactions.size());
    }
}
