package com.kodilla.currencyexchange.domain;

import jakarta.persistence.*;
import lombok.Builder;
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
    private Long Id;

    @NonNull
    @Column(unique = true)
    private String address;

    @NonNull
    private String phone;

    private boolean active = true;

    @Builder

    public StationaryOffice(Long id, @NonNull String address, @NonNull String phone, boolean active) {
        Id = id;
        this.address = address;
        this.phone = phone;
        this.active = active;
    }
}
