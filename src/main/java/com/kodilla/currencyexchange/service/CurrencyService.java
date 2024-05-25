package com.kodilla.currencyexchange.service;

import com.kodilla.currencyexchange.domain.Currency;
import com.kodilla.currencyexchange.exception.CurrencyAlreadyExistException;
import com.kodilla.currencyexchange.exception.CurrencyNotFoundException;
import com.kodilla.currencyexchange.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
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

    public Currency getCurrencyById(final Long id) throws CurrencyNotFoundException {
        return currencyRepository.findByIdAndActiveTrue(id).orElseThrow(CurrencyNotFoundException::new);
    }

    public Currency getCurrencyByCode(final String code) throws CurrencyNotFoundException {
        return currencyRepository.findByCodeAndActiveTrue(code).orElseThrow(CurrencyNotFoundException::new);
    }

    public Currency saveCurrency(final Currency currency) {
        if (currencyRepository.findByCodeAndActiveTrue(currency.getCode()).isPresent()) {
            log.error("Currency with code {} already exist.", currency.getCode());
            throw new CurrencyAlreadyExistException();
        }
        Currency savedCurrency = currencyRepository.save(currency);
        log.info("Currency with code {} added successfully.", currency.getCode());
        return savedCurrency;
    }

    public void deleteCurrency(final Long currencyId) throws CurrencyNotFoundException {
        currencyRepository.delete(currencyRepository.findByIdAndActiveTrue(currencyId).
                orElseThrow(CurrencyNotFoundException::new));
    }
}
