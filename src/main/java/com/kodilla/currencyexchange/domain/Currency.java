package com.kodilla.currencyexchange.domain;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "CURRENCIES")
@NoArgsConstructor
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;

    @NonNull
    @Column(unique = true)
    private String code;

    @NonNull
    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "baseCurrency")
    private List<ExchangeRate> baseExchangeRates = new ArrayList<>();

    @OneToMany(mappedBy = "targetCurrency")
    private List<ExchangeRate> targetExchangeRates = new ArrayList<>();

    private boolean active = true;

    private boolean crypto;

    @Builder

    public Currency(Long id, @NonNull String code, @NonNull String name, List<ExchangeRate> baseExchangeRates,
                    List<ExchangeRate> targetExchangeRates, boolean active, boolean crypto) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.baseExchangeRates = baseExchangeRates;
        this.targetExchangeRates = targetExchangeRates;
        this.active = active;
        this.crypto = crypto;
    }
}
