package com.kodilla.currencyexchange.repository;

import com.kodilla.currencyexchange.domain.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    default void delete(Currency entity) {
        entity.setActive(false);
        save(entity);
    }

    List<Currency> findAllByActiveTrue();

    Optional<Currency> findByCodeAndActiveTrue(String code);

    List<Currency> findAllByCryptoTrueAndActiveTrue();

    List<Currency> findAllByCryptoFalseAndActiveTrue();

    Optional<Currency> findByIdAndActiveTrue(Long id);

}
