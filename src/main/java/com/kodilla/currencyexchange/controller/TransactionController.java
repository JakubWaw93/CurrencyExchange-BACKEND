package com.kodilla.currencyexchange.controller;

import com.kodilla.currencyexchange.domain.Transaction;
import com.kodilla.currencyexchange.domain.TransactionDto;
import com.kodilla.currencyexchange.exception.CurrencyNotFoundException;
import com.kodilla.currencyexchange.exception.ExchangeRateNotFoundException;
import com.kodilla.currencyexchange.exception.TransactionNotFoundException;
import com.kodilla.currencyexchange.exception.UserNotFoundException;
import com.kodilla.currencyexchange.mapper.TransactionMapper;
import com.kodilla.currencyexchange.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@CrossOrigin("*")
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @GetMapping
    public ResponseEntity<List<TransactionDto>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransaction();
        return ResponseEntity.ok(transactionMapper.mapToTransactionDtoList(transactions));
    }

    @GetMapping("/userid/{userId}")
    public ResponseEntity<List<TransactionDto>> getAllTransactionsByUserId(@PathVariable Long userId) {
        List<Transaction> transactions = transactionService.getAllTransactionsByUserId(userId);
        return ResponseEntity.ok(transactionMapper.mapToTransactionDtoList(transactions));
    }

    @GetMapping("/userlogin/{userLogin}")
    public ResponseEntity<List<TransactionDto>> getAllTransactionsByUserLogin(@PathVariable String userLogin) {
        List<Transaction> transactions = transactionService.getAllTransactionsByUserLogin(userLogin);
        return ResponseEntity.ok(transactionMapper.mapToTransactionDtoList(transactions));
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionDto> getTransactionById(@PathVariable Long transactionId) throws TransactionNotFoundException {
        Transaction transaction = transactionService.getTransactionById(transactionId);
        return ResponseEntity.ok(transactionMapper.mapToTransactionDto(transaction));
    }

    @PostMapping
    public ResponseEntity<Void> createTransaction(@RequestBody TransactionDto transactionDto) throws UserNotFoundException, ExchangeRateNotFoundException, CurrencyNotFoundException {
        Transaction transaction = transactionMapper.mapToTransaction(transactionDto);
        transactionService.saveTransaction(transaction);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<TransactionDto> updateTransaction(@RequestBody TransactionDto transactionDto) throws UserNotFoundException, ExchangeRateNotFoundException, CurrencyNotFoundException, TransactionNotFoundException {
        Transaction transaction = transactionMapper.mapToTransaction(transactionDto);
        transactionService.saveTransaction(transaction);
        Transaction savedTransaction = transactionService.getTransactionById(transaction.getId());
        return ResponseEntity.ok(transactionMapper.mapToTransactionDto(savedTransaction));
    }
}
