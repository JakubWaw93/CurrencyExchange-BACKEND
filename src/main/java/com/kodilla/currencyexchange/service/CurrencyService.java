package com.kodilla.currencyexchange.service;

import com.kodilla.currencyexchange.domain.Currency;
import com.kodilla.currencyexchange.exception.CurrencyNotFoundException;
import com.kodilla.currencyexchange.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyRepository currencyRepository;

    public List<Currency> getAllCurrencies() {
        return currencyRepository.findAllByActiveTrue();
    }

    public List<Currency> getAllStandardCurrencies() {
        return currencyRepository.findAllByCryptoFalseAndActiveTrue();
    }

    public List<Currency> getAllCryptoCurrencies() {
        return currencyRepository.findAllByCryptoTrueAndActiveTrue();
    }

    public Currency getCurrencyById(final Long id) {
        return currencyRepository.findByIdAndActiveTrue(id).orElseThrow(CurrencyNotFoundException::new);
    }

    public Currency getCurrencyByCode(final String code) {
        return currencyRepository.findByCodeAndActiveTrue(code).orElseThrow(CurrencyNotFoundException::new);
    }

    public Currency saveCurrency(final Currency currency) {
        return currencyRepository.save(currency);
    }

    public void deleteCurrency(final Long currencyId) throws CurrencyNotFoundException {
        currencyRepository.delete(currencyRepository.findByIdAndActiveTrue(currencyId).
                orElseThrow(CurrencyNotFoundException::new));
    }
}
