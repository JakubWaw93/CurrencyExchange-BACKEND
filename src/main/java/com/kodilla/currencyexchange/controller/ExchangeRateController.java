package com.kodilla.currencyexchange.controller;

import com.kodilla.currencyexchange.domain.ExchangeRate;
import com.kodilla.currencyexchange.domain.ExchangeRateDto;
import com.kodilla.currencyexchange.exception.CurrencyNotFoundException;
import com.kodilla.currencyexchange.exception.ExchangeRateCalculationFailedException;
import com.kodilla.currencyexchange.exception.ExchangeRateNotFoundException;
import com.kodilla.currencyexchange.mapper.ExchangeRateMapper;
import com.kodilla.currencyexchange.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exchangerates")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;
    private final ExchangeRateMapper exchangeRateMapper;

    @GetMapping
    public ResponseEntity<List<ExchangeRateDto>> getAllExchangeRates() {
        List<ExchangeRate> exchangeRateList = exchangeRateService.getAllExchangeRates();
        return ResponseEntity.ok(exchangeRateMapper.mapToExchangeRateDtoList(exchangeRateList));
    }

    @GetMapping("/{exchangeRateId}")
    public ResponseEntity<ExchangeRateDto> getExchangeRateById(@PathVariable Long exchangeRateId) throws ExchangeRateNotFoundException {
        ExchangeRate exchangeRate = exchangeRateService.getExchangeRateById(exchangeRateId);
        return ResponseEntity.ok(exchangeRateMapper.mapToExchangeRateDto(exchangeRate));
    }

    @GetMapping("/codes/{baseCode}/{targetCode}")
    public ResponseEntity<ExchangeRateDto> getExchangeRateByCodes(@PathVariable String baseCode, @PathVariable String targetCode) throws ExchangeRateNotFoundException, CurrencyNotFoundException, ExchangeRateCalculationFailedException {
        ExchangeRate exchangeRate = exchangeRateService.getExchangeRateByCurrencyCodes(baseCode, targetCode);
        return ResponseEntity.ok(exchangeRateMapper.mapToExchangeRateDto(exchangeRate));
    }

    @GetMapping("code/base/{baseCode}")
    public ResponseEntity<List<ExchangeRateDto>> getRatesByBaseCurrencyCode(@PathVariable String baseCode) {
        List<ExchangeRate> exchangeRatesForThisCurrency = exchangeRateService.getExchangeRatesByBaseCurrencyCode(baseCode);
        return ResponseEntity.ok(exchangeRateMapper.mapToExchangeRateDtoList(exchangeRatesForThisCurrency));
    }

    @GetMapping("code/target/{targetCode}")
    public ResponseEntity<List<ExchangeRateDto>> getRatesByTargetCurrencyCode(@PathVariable String targetCode) {
        List<ExchangeRate> exchangeRatesForThisCurrency = exchangeRateService.getExchangeRatesByTargetCurrencyCode(targetCode);
        return ResponseEntity.ok(exchangeRateMapper.mapToExchangeRateDtoList(exchangeRatesForThisCurrency));
    }

    @PostMapping
    public ResponseEntity<ExchangeRateDto> createExchangeRate(@RequestBody ExchangeRateDto exchangeRateDto) throws CurrencyNotFoundException {
        ExchangeRate exchangeRate = exchangeRateMapper.mapToExchangeRate(exchangeRateDto);
        ExchangeRate savedExchangeRate = exchangeRateService.saveExchangeRate(exchangeRate);
        return ResponseEntity.ok(exchangeRateMapper.mapToExchangeRateDto(savedExchangeRate));
    }

}
