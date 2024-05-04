package com.kodilla.currencyexchange.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@Entity
@Audited
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
    private String login;

    @NonNull
    @NotAudited
    private String password;

    @NonNull
    @Column(unique = true)
    private String emailAddress;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Transaction> transactions = new ArrayList<>();

    @NotAudited
    @Column(unique = true)
    private String apiKey;

    @Builder.Default
    private boolean active = true;

    @NotAudited
    @Transient
    private LocalDateTime apiKeyExpiration;


    public User(Long id, @NonNull String firstname, @NonNull String lastname, @NonNull String login,
                @NonNull String password, @NonNull String emailAddress, List<Transaction> transactions,
                String apiKey, boolean active, LocalDateTime apiKeyExpiration) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.login = login;
        this.password = password;
        this.emailAddress = emailAddress;
        this.transactions = new ArrayList<>();
        this.apiKey = apiKey;
        this.active = true;
        this.apiKeyExpiration = apiKeyExpiration;
    }
}
