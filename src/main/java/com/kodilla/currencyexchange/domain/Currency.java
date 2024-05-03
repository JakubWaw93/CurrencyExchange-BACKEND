package com.kodilla.currencyexchange.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.envers.Audited;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@Entity
@Audited
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

    @Builder.Default
    @OneToMany(mappedBy = "baseCurrency", cascade = CascadeType.ALL)
    private List<ExchangeRate> baseExchangeRates = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "targetCurrency", cascade = CascadeType.ALL)
    private List<ExchangeRate> targetExchangeRates = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "boughtCurrency", cascade = CascadeType.ALL)
    private List<Transaction> boughtInTransactions = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "soldCurrency", cascade = CascadeType.ALL)
    private List<Transaction> soldInTransactions = new ArrayList<>();

    @Builder.Default
    private boolean active = true;

    private boolean crypto;

    public Currency(Long id, @NonNull String code, @NonNull String name, List<ExchangeRate> baseExchangeRates, List<ExchangeRate> targetExchangeRates, List<Transaction> boughtInTransactions, List<Transaction> soldInTransactions, boolean active, boolean crypto) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.baseExchangeRates = baseExchangeRates;
        this.targetExchangeRates = targetExchangeRates;
        this.boughtInTransactions = boughtInTransactions;
        this.soldInTransactions = soldInTransactions;
        this.active = active;
        this.crypto = crypto;
    }
}
