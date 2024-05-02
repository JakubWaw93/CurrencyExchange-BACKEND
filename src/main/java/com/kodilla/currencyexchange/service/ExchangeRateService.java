package com.kodilla.currencyexchange.service;

import com.kodilla.currencyexchange.domain.Currency;
import com.kodilla.currencyexchange.domain.ExchangeRate;
import com.kodilla.currencyexchange.exception.CurrencyNotFoundException;
import com.kodilla.currencyexchange.exception.ExchangeRateNotFoundException;
import com.kodilla.currencyexchange.repository.CurrencyRepository;
import com.kodilla.currencyexchange.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;
    private final CurrencyRepository currencyRepository;

    public List<ExchangeRate> getAllExchangeRates() {
        return exchangeRateRepository.findAll();
    }

    public ExchangeRate getExchangeRateById(final Long id) throws ExchangeRateNotFoundException {
        return exchangeRateRepository.findById(id).orElseThrow(ExchangeRateNotFoundException::new);
    }

    public ExchangeRate getExchangeRateByCurrencyCodes(final String baseCurrencyCode, final String targetCurrencyCode) throws ExchangeRateNotFoundException, CurrencyNotFoundException {
        return exchangeRateRepository.findByBaseCurrencyCodeAndTargetCurrencyCode
                (baseCurrencyCode, targetCurrencyCode).orElseThrow
                (ExchangeRateNotFoundException::new);
    }

    public List<ExchangeRate> getExchangeRatesForCurrencyByItsCode(final String currencyCode) {
        return exchangeRateRepository.findAllByBaseCurrencyCode(currencyCode);
    }

    public ExchangeRate saveExchangeRate(final ExchangeRate exchangeRate) {
        return exchangeRateRepository.save(exchangeRate);
    }

    @Scheduled(cron = "0/15 0 * * ?")
    public void calculateAndSaveMissingExchangeRates() {
        List<ExchangeRate> plnExchangeRates = exchangeRateRepository.findAllByTargetCurrencyCode("PLN");

        for (ExchangeRate plnExchangeRate : plnExchangeRates) {
            Currency baseCurrency = plnExchangeRate.getBaseCurrency();

            List<Currency> targetCurrencies = currencyRepository.findAllByCodeNot("PLN");
            for (Currency targetCurrency : targetCurrencies) {
                ExchangeRate existingExchangeRate = exchangeRateRepository
                        .findByBaseCurrencyCodeAndTargetCurrencyCode(baseCurrency.getCode(), targetCurrency.getCode())
                        .orElse(null);

                if (existingExchangeRate != null) {
                    BigDecimal newRate = calculateNewExchangeRate(plnExchangeRate, targetCurrency);
                    existingExchangeRate.setRate(newRate);

                    exchangeRateRepository.save(existingExchangeRate);
                } else {
                    BigDecimal newRate = calculateNewExchangeRate(plnExchangeRate, targetCurrency);
                    ExchangeRate newExchangeRate = ExchangeRate.builder()
                            .rate(newRate)
                            .lastUpdateTime(LocalDateTime.now())
                            .baseCurrency(baseCurrency)
                            .targetCurrency(targetCurrency)
                            .build();

                    exchangeRateRepository.save(newExchangeRate);
                }
            }
        }
    }


    private BigDecimal calculateNewExchangeRate(ExchangeRate plnExchangeRate, Currency targetCurrency) {
        BigDecimal plnRate = plnExchangeRate.getRate();

        BigDecimal inversePlnRate = BigDecimal.ONE.divide(plnRate, 10, RoundingMode.HALF_UP);

        ExchangeRate targetToPlnExchangeRate = exchangeRateRepository
                .findByBaseCurrencyCodeAndTargetCurrencyCode(targetCurrency.getCode(), "PLN")
                .orElseThrow(() -> new IllegalStateException("Exchange rate not found for " + targetCurrency.getCode() + " to PLN"));

        BigDecimal result = targetToPlnExchangeRate.getRate().multiply(inversePlnRate).setScale(10, RoundingMode.HALF_UP);

        return BigDecimal.ONE.divide(result, 10, RoundingMode.HALF_UP).setScale(10, RoundingMode.HALF_UP);
    }


}
