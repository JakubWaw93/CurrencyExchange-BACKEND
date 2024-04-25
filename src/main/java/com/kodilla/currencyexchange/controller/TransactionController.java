package com.kodilla.currencyexchange.controller;

import com.kodilla.currencyexchange.domain.ExchangeRateDto;
import com.kodilla.currencyexchange.domain.Transaction;
import com.kodilla.currencyexchange.domain.TransactionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Transaction>> getAllTransactionsFromUser(@PathVariable Long userId) {
        List<Transaction> transactions = new ArrayList<>();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionDto> getTransactionById(@PathVariable Long transactionId) {
        TransactionDto transactionDto = TransactionDto.builder()
                .id(transactionId)
                .boughtCurrencyId(1L)
                .soldCurrencyId(2L)
                .exchangeRateId(1L)
                .userId(1L)
                .amount(new BigDecimal(10))
                .status("Completed")
                .transactionDate(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(transactionDto);
    }

    @PostMapping
    public ResponseEntity<TransactionDto> createTransaction(@RequestBody TransactionDto transactionDto) {
        return ResponseEntity.ok(transactionDto);
    }

    @PutMapping
    public ResponseEntity<TransactionDto> updateTransaction(@RequestBody TransactionDto transactionDto) {
        return ResponseEntity.ok(transactionDto);
    }
}
