package com.kodilla.currencyexchange.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@Entity
@Table(name = "USERS")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;

    @NonNull
    private String firstname;

    @NonNull
    private String lastname;

    @NonNull
    @Column(unique = true)
    private String emailAddress;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Transaction> transactions = new ArrayList<>();

    @NonNull
    @Column(unique = true)
    private String apiKey;

    @Builder.Default
    private boolean active = true;

    @Transient
    private LocalDateTime apiKeyExpiration;

    @Builder
    public User(Long id, @NonNull String firstname, @NonNull String lastname, @NonNull String emailAddress, List<Transaction> transactions, @NonNull String apiKey, boolean active, LocalDateTime apiKeyExpiration) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.emailAddress = emailAddress;
        this.transactions = transactions;
        this.apiKey = apiKey;
        this.active = active;
        this.apiKeyExpiration = apiKeyExpiration;
    }
}
