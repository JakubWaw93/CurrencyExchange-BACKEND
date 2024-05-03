package com.kodilla.currencyexchange.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.envers.Audited;

@Builder
@Data
@Entity
@Audited
@Table(name = "STATIONARY_OFFICES")
@NoArgsConstructor
public class StationaryOffice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @NonNull
    @Column(unique = true)
    private String address;

    @NonNull
    private String phone;

    @Builder.Default
    private boolean active = true;

    public StationaryOffice(Long id, @NonNull String address, @NonNull String phone, boolean active) {
        this.id = id;
        this.address = address;
        this.phone = phone;
        this.active = true;
    }
}
