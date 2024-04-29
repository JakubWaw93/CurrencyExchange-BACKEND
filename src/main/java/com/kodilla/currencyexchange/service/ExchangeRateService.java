package com.kodilla.currencyexchange.service;

import com.kodilla.currencyexchange.domain.ExchangeRate;
import com.kodilla.currencyexchange.exception.CurrencyNotFoundException;
import com.kodilla.currencyexchange.exception.ExchangeRateNotFoundException;
import com.kodilla.currencyexchange.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;
    private final CurrencyService currencyService;

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

    public void calculateRates(final String baseCurrencyCode, final String targetCurrencyCode) throws CurrencyNotFoundException, ExchangeRateNotFoundException {
        BigDecimal exchangeRateForPLN = getExchangeRateByCurrencyCodes(baseCurrencyCode, "PLN").getRate();
        BigDecimal newRate;
        if (!targetCurrencyCode.equals("PLN")) {
            if (currencyService.getCurrencyByCode(targetCurrencyCode).isCrypto()) {

                BigDecimal exchangeRateForPLNToUSD = new BigDecimal(1L).divide(getExchangeRateByCurrencyCodes("USD", "PLN").getRate());
                newRate = exchangeRateForPLN.divide(exchangeRateForPLNToUSD);

                BigDecimal exchangeRateFromUsdToCrypto = new BigDecimal(1L).divide(getExchangeRateByCurrencyCodes("USD", targetCurrencyCode).getRate());
                newRate = newRate.multiply(exchangeRateFromUsdToCrypto);
            } else {
                BigDecimal exchangeRateForTargetCurrency = getExchangeRateByCurrencyCodes(targetCurrencyCode, "PLN").getRate();
                newRate = exchangeRateForPLN.divide(exchangeRateForTargetCurrency);
            }
            ExchangeRate exchangeRate = ExchangeRate.builder()
                    .baseCurrency(currencyService.getCurrencyByCode(baseCurrencyCode))
                    .targetCurrency(currencyService.getCurrencyByCode(targetCurrencyCode))
                    .rate(newRate)
                    .lastUpdateTime(LocalDateTime.now())
                    .build();

            saveExchangeRate(exchangeRate);
        }
        //może jednak zrezygnuję z tych zamian, i dla ułatwienia dla wszystkich ExchangeRate target będzie w PLN

    }


}
