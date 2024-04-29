package com.kodilla.currencyexchange.controller;

import com.kodilla.currencyexchange.domain.Currency;
import com.kodilla.currencyexchange.domain.CurrencyDto;
import com.kodilla.currencyexchange.exception.CurrencyNotFoundException;
import com.kodilla.currencyexchange.mapper.CurrencyMapper;
import com.kodilla.currencyexchange.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;
    private final CurrencyMapper currencyMapper;

    @GetMapping
    public ResponseEntity<List<CurrencyDto>> getAllCurrencies() {
        List<Currency> currencyList = currencyService.getAllCurrencies();
        return ResponseEntity.ok(currencyMapper.mapToCurrencyDtoList(currencyList));
    }

    @GetMapping("/standard")
    public ResponseEntity<List<CurrencyDto>> getAllStandardCurrencies() {
        List<Currency> currencyList = currencyService.getAllStandardCurrencies();
        return ResponseEntity.ok(currencyMapper.mapToCurrencyDtoList(currencyList));
    }

    @GetMapping("/crypto")
    public ResponseEntity<List<CurrencyDto>> getAllCryptoCurrencies() {
        List<Currency> currencyList = currencyService.getAllCryptoCurrencies();
        return ResponseEntity.ok(currencyMapper.mapToCurrencyDtoList(currencyList));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<CurrencyDto> getCurrencyByCode(@PathVariable String code) throws CurrencyNotFoundException {
        return ResponseEntity.ok(currencyMapper.mapToCurrencyDto(currencyService.getCurrencyByCode(code)));
    }

    @GetMapping("/{currencyId}")
    public ResponseEntity<CurrencyDto> getCurrencyById(@PathVariable Long currencyId) throws CurrencyNotFoundException {
        return ResponseEntity.ok(currencyMapper.mapToCurrencyDto(currencyService.getCurrencyById(currencyId)));
    }

    @PostMapping
    public ResponseEntity<Void> createCurrency(@RequestBody CurrencyDto currencyDto) {
        Currency currency = currencyMapper.mapToCurrency(currencyDto);
        currencyService.saveCurrency(currency);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<CurrencyDto> updateCurrency(@RequestBody CurrencyDto currencyDto) {
        Currency currency = currencyMapper.mapToCurrency(currencyDto);
        Currency savedCurrency = currencyService.saveCurrency(currency);
        return ResponseEntity.ok(currencyMapper.mapToCurrencyDto(savedCurrency));
    }

    @DeleteMapping("/{currencyId}")
    public ResponseEntity<Void> deleteCurrency(@PathVariable Long currencyId) throws CurrencyNotFoundException {
        currencyService.deleteCurrency(currencyId);
        return ResponseEntity.ok().build();
    }
}
