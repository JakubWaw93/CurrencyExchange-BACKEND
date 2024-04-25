package com.kodilla.currencyexchange.controller;

import com.kodilla.currencyexchange.domain.CurrencyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/currencies")
public class CurrencyController {

    @GetMapping
    public ResponseEntity<List<CurrencyDto>> getAllCurrencies() {
        List<CurrencyDto> currencyDtoList = new ArrayList<>();
        return ResponseEntity.ok(currencyDtoList);
    }

    @GetMapping("/standard")
    public ResponseEntity<List<CurrencyDto>> getAllStandardCurrencies() {
        List<CurrencyDto> currencyDtoList = new ArrayList<>();
        return ResponseEntity.ok(currencyDtoList);
    }

    @GetMapping("/crypto")
    public ResponseEntity<List<CurrencyDto>> getAllCryptoCurrencies() {
        List<CurrencyDto> currencyDtoList = new ArrayList<>();
        return ResponseEntity.ok(currencyDtoList);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<CurrencyDto> getCurrencyByCode(@PathVariable String code) {
        CurrencyDto currencyDto = CurrencyDto.builder()
                .Id(1L)
                .code(code)
                .name("Polski złoty")
                .build();
        return ResponseEntity.ok(currencyDto);
    }

    @GetMapping("/{currencyId}")
    public ResponseEntity<CurrencyDto> getCurrencyById(@PathVariable Long currencyId) {
        CurrencyDto currencyDto = CurrencyDto.builder()
                .Id(currencyId)
                .code("PLN")
                .name("Polski złoty")
                .build();
        return ResponseEntity.ok(currencyDto);
    }

    @PostMapping
    public ResponseEntity<CurrencyDto> createCurrency(@RequestBody CurrencyDto currencyDto) {
        return ResponseEntity.ok(currencyDto);
    }

    @PutMapping
    public ResponseEntity<CurrencyDto> updateCurrency(@RequestBody CurrencyDto currencyDto) {
        return ResponseEntity.ok(currencyDto);
    }

    @DeleteMapping("/{currencyId}")
    public ResponseEntity<String> deleteCurrency(@PathVariable String currencyId) {
        return ResponseEntity.ok("Currency with id: " + currencyId + " was successfully deleted");
    }
}
