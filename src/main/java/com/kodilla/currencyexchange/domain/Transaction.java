package com.kodilla.currencyexchange.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "TRANSACTIONS")
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "bought_currency_id")
    private Currency boughtCurrency;

    @ManyToOne
    @JoinColumn(name = "sold_currency_id")
    private Currency soldCurrency;

    @ManyToOne
    @JoinColumn(name = "exchange_rate_id")
    private ExchangeRate exchangeRate;

    @NonNull
    private BigDecimal amount;

    @NonNull
    private String status;

    @NonNull
    private LocalDateTime transactionDate = LocalDateTime.now();

    @Builder

    public Transaction(Long id, User user, Currency boughtCurrency, Currency soldCurrency, ExchangeRate exchangeRate,
                       @NonNull BigDecimal amount, @NonNull String status, @NonNull LocalDateTime transactionDate) {
        this.id = id;
        this.user = user;
        this.boughtCurrency = boughtCurrency;
        this.soldCurrency = soldCurrency;
        this.exchangeRate = exchangeRate;
        this.amount = amount;
        this.status = status;
        this.transactionDate = transactionDate;
    }
}
