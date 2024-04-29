package com.kodilla.currencyexchange.repository;

import com.kodilla.currencyexchange.domain.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

    Optional<ExchangeRate> findByBaseCurrencyIdAndTargetCurrencyId(Long baseCurrencyId, Long targetCurrencyId);
    Optional<ExchangeRate> findByBaseCurrencyCodeAndTargetCurrencyCode(String baseCurrencyCode, String targetCurrencyCode);

    List<ExchangeRate> findAllByBaseCurrencyId(Long currencyId);
    List<ExchangeRate> findAllByBaseCurrencyCode(String currencyCode);


}