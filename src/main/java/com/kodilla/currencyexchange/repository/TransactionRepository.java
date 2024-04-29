package com.kodilla.currencyexchange.repository;

import com.kodilla.currencyexchange.domain.Transaction;
import com.kodilla.currencyexchange.domain.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByUserId(Long userId);
    List<Transaction> findAllByStatusIs(TransactionStatus transactionStatus);

}
