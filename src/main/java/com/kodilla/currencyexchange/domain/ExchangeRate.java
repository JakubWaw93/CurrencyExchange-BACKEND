package com.kodilla.currencyexchange.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@Entity
@Audited
@Table(name = "EXCHANGE_RATES")
@NoArgsConstructor
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;

    @NonNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "base_currency_id")
    private Currency baseCurrency;

    @NonNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "target_currency_id")
    private Currency targetCurrency;

    @Builder.Default
    @OneToMany(mappedBy = "exchangeRate")
    private List<Transaction> transactions = new ArrayList<>();

    @NonNull
    private BigDecimal rate;

    @NonNull
    private LocalDateTime lastUpdateTime;

    public ExchangeRate(Long id, @NonNull Currency baseCurrency, @NonNull Currency targetCurrency,
                        List<Transaction> transactions, @NonNull BigDecimal rate, @NonNull LocalDateTime lastUpdateTime) {
        this.id = id;
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.transactions = transactions;
        this.rate = rate;
        this.lastUpdateTime = lastUpdateTime;
    }
}

