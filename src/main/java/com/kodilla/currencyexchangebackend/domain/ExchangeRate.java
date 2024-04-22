package com.kodilla.currencyexchangebackend.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
@Entity
@Table(name = "EXCHANGE_RATES")
@NoArgsConstructor
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "base_currency_id")
    private Currency baseCurrency;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "target_currency_id")
    private Currency targetCurrency;

    @NonNull
    private BigDecimal rate;

    @NonNull
    private LocalDate localDate;
}
