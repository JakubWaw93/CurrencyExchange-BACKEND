package com.kodilla.currencyexchange.mapper;

import com.kodilla.currencyexchange.domain.Transaction;
import com.kodilla.currencyexchange.domain.User;
import com.kodilla.currencyexchange.domain.UserDto;
import com.kodilla.currencyexchange.repository.TransactionRepository;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class UserMapper {

    public final TransactionRepository transactionRepository;

    public User mapToUser(final UserDto userDto) {
        List<Transaction> transactions = userDto.getTransactionsIds().stream()
                .map(transactionRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        return User.builder()
                .id(userDto.getId())
                .firstname(userDto.getFirstname())
                .lastname(userDto.getLastname())
                .emailAddress(userDto.getEmailAddress())
                .active(userDto.isActive())
                .apiKey(userDto.getApiKey())
                .transactions(transactions)
                .build();
    }

    public UserDto mapToUserDto(final User user) {
        List<Long> transactionsIds = user.getTransactions().stream()
                .map(Transaction::getId)
                .toList();
        return UserDto.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .emailAddress(user.getEmailAddress())
                .active(user.isActive())
                .apiKey(user.getApiKey())
                .transactionsIds(transactionsIds)
                .build();
    }

    public List<UserDto> mapToUserDtoList(final List<User> users) {
        return users.stream()
                .map(this::mapToUserDto)
                .toList();
    }
}
