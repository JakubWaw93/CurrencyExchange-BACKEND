package com.kodilla.currencyexchange.domain;

import jakarta.persistence.*;
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
    private long Id;

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

}
