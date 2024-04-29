package com.kodilla.currencyexchange.controller;

import com.kodilla.currencyexchange.domain.ExchangeRateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/exchangerates")
@RequiredArgsConstructor
public class ExchangeRateController {

    @GetMapping
    public ResponseEntity<List<ExchangeRateDto>> getAllExchangeRates() {
        List<ExchangeRateDto> exchangeRateDtoList = new ArrayList<>();
        return ResponseEntity.ok(exchangeRateDtoList);
    }

    @GetMapping("/{ExchangeRateId}")
    public ResponseEntity<ExchangeRateDto> getExchangeRateById(@PathVariable Long ExchangeRateId) {
        ExchangeRateDto exchangeRateDto = ExchangeRateDto.builder()
                .id(ExchangeRateId)
                .lastUpdateTime(LocalDateTime.now())
                .baseCurrencyId(1L)
                .targetCurrencyId(2L)
                .rate(new BigDecimal("500.256"))
                .build();
        return ResponseEntity.ok(exchangeRateDto);
    }

    @GetMapping("/ids/{baseId}/{targetId}")
    public ResponseEntity<ExchangeRateDto> getExchangeRateByIds(@PathVariable Long baseId, @PathVariable Long targetId) {
        ExchangeRateDto exchangeRateDto = ExchangeRateDto.builder()
                .id(1L)
                .lastUpdateTime(LocalDateTime.now())
                .baseCurrencyId(baseId)
                .targetCurrencyId(targetId)
                .rate(new BigDecimal("500.256"))
                .build();
        return ResponseEntity.ok(exchangeRateDto);
    }

    @GetMapping("/codes/{baseCode}/{targetCode}")
    public ResponseEntity<ExchangeRateDto> getExchangeRateByCodes(@PathVariable String baseCode, @PathVariable String targetCode) {
        //konwersja poprzez wyszukanie konkretnej waluty i jej Id na podstawie Code.
        return ResponseEntity.ok().build();
    }

    @GetMapping("currencyId/{baseId}")
    public ResponseEntity<List<ExchangeRateDto>> getRatesForCurrencyById(@PathVariable Long baseId) {
        List<ExchangeRateDto> exchangeRatesDtosForThisCurrency = new ArrayList<>();
        return ResponseEntity.ok(exchangeRatesDtosForThisCurrency);
    }

    @GetMapping("currencyCode/{baseCode}")
    public ResponseEntity<List<ExchangeRateDto>> getRatesForCurrencyByCode(@PathVariable Long baseCode) {
        List<ExchangeRateDto> exchangeRatesDtosForThisCurrency = new ArrayList<>();
        return ResponseEntity.ok(exchangeRatesDtosForThisCurrency);
    }

    @PostMapping
    public ResponseEntity<ExchangeRateDto> createExchangeRate() {
        ExchangeRateDto exchangeRateDto = ExchangeRateDto.builder()
                .id(1L)
                .lastUpdateTime(LocalDateTime.now())
                .baseCurrencyId(1L)
                .targetCurrencyId(2L)
                .rate(new BigDecimal("500.256"))
                .build();
        return ResponseEntity.ok(exchangeRateDto);
    }

    @PutMapping
    public ResponseEntity<ExchangeRateDto> updateExchangeRate(@RequestBody ExchangeRateDto exchangeRateDto) {
        return ResponseEntity.ok(exchangeRateDto);
        //nie wiem jeszzcze czy będę tego używał, plan jest zrobić w service automatyczny
        //update ExchangeRates za pomocą shedulera, może tu zrobię update na żądanie, ale wtedy
        // chyba bez @RequestBody tylko @PutMapping(update).
    }

    //nie widzę powodu by usuwać pozycję w tej encji ale jeszcze pomyślimy
}
