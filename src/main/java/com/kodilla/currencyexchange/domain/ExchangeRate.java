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
    private LocalDateTime localDateTime;

    @Builder
    public ExchangeRate(Long id, @NonNull Currency baseCurrency, @NonNull Currency targetCurrency,
                        @NonNull BigDecimal rate, @NonNull LocalDateTime localDateTime) {
        this.id = id;
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
        this.localDateTime = localDateTime;
    }
}
