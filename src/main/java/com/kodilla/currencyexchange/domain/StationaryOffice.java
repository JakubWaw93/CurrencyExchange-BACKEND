package com.kodilla.currencyexchange.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Entity
@Table(name = "STATIONARY_OFFICES")
@NoArgsConstructor
public class StationaryOffice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private long Id;

    @NonNull
    @Column(unique = true)
    private String address;

    @NonNull
    private String phone;

}
