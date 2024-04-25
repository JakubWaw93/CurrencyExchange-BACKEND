package com.kodilla.currencyexchange.repository;

import com.kodilla.currencyexchange.domain.StationaryOffice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StationaryOfficeRepository extends JpaRepository<StationaryOffice, Long> {

    default void delete(StationaryOffice entity) {
        entity.setActive(false);
        save(entity);
    }

    Optional<StationaryOffice> findByIdAndActiveTrue(Long id);

    List<StationaryOffice> findAllByActiveTrue();
}
