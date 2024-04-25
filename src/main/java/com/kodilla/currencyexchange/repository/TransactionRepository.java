package com.kodilla.currencyexchange.repository;

import com.kodilla.currencyexchange.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {


}
