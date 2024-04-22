package com.kodilla.currencyexchangebackend.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;

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

    @NonNull
    @Column(unique = true)
    private String apiKey;

    private boolean active = true;

    @Transient
    private LocalDateTime apiKeyExpiration;
}
