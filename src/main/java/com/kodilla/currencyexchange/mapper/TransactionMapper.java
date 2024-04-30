package com.kodilla.currencyexchange.mapper;

import com.kodilla.currencyexchange.domain.Transaction;
import com.kodilla.currencyexchange.domain.TransactionDto;
import com.kodilla.currencyexchange.domain.TransactionStatus;
import com.kodilla.currencyexchange.exception.CurrencyNotFoundException;
import com.kodilla.currencyexchange.exception.ExchangeRateNotFoundException;
import com.kodilla.currencyexchange.exception.UserNotFoundException;
import com.kodilla.currencyexchange.repository.CurrencyRepository;
import com.kodilla.currencyexchange.repository.ExchangeRateRepository;
import com.kodilla.currencyexchange.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TransactionMapper {

    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;
    private final ExchangeRateRepository exchangeRateRepository;

    public Transaction mapToTransaction(final TransactionDto transactionDto) throws UserNotFoundException, ExchangeRateNotFoundException, CurrencyNotFoundException {
        return Transaction.builder()
                .id(transactionDto.getId())
                .user(userRepository.findByIdAndActiveTrue(transactionDto.getUserId()).orElseThrow(UserNotFoundException::new))
                .boughtCurrency(currencyRepository.findByIdAndActiveTrue(transactionDto.getBoughtCurrencyId()).orElseThrow(CurrencyNotFoundException::new))
                .soldCurrency(currencyRepository.findByIdAndActiveTrue(transactionDto.getSoldCurrencyId()).orElseThrow(CurrencyNotFoundException::new))
                .exchangeRate(exchangeRateRepository.findById(transactionDto.getExchangeRateId()).orElseThrow(ExchangeRateNotFoundException::new))
                .amountBoughtCurrency(transactionDto.getAmountBoughtCurrency())
                .status(TransactionStatus.valueOf(transactionDto.getStatus()))
                .transactionDate(transactionDto.getTransactionDate())
                .build();
    }

    public TransactionDto mapToTransactionDto(final Transaction transaction) {
        return TransactionDto.builder()
                .id(transaction.getId())
                .userId(transaction.getUser().getId())
                .boughtCurrencyId(transaction.getBoughtCurrency().getId())
                .soldCurrencyId(transaction.getSoldCurrency().getId())
                .exchangeRateId(transaction.getExchangeRate().getId())
                .amountBoughtCurrency(transaction.getAmountBoughtCurrency())
                .status(transaction.getStatus().toString())
                .transactionDate(transaction.getTransactionDate())
                .build();
    }

    public List<TransactionDto> mapToTransactionDtoList(final List<Transaction> transactions) {
        return transactions.stream()
                .map(this::mapToTransactionDto)
                .toList();
    }
}
