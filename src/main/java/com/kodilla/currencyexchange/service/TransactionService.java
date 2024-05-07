package com.kodilla.currencyexchange.service;

import com.kodilla.currencyexchange.domain.Transaction;
import com.kodilla.currencyexchange.exception.TransactionNotFoundException;
import com.kodilla.currencyexchange.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public List<Transaction> getAllTransaction() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getAllTransactionsByUserId(final Long userId) {
        return transactionRepository.findAllByUserId(userId);
    }

    public List<Transaction> getAllTransactionsByUserLogin(final String login) {
        return transactionRepository.findAllByUserLogin(login);
    }

    public Transaction getTransactionById(final Long transactionId) throws TransactionNotFoundException {
        return transactionRepository.findById(transactionId).orElseThrow(TransactionNotFoundException::new);
    }

    public Transaction saveTransaction(final Transaction transaction) {
        return transactionRepository.save(transaction);
    }

}
