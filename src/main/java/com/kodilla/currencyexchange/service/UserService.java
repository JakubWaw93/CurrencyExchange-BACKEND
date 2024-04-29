package com.kodilla.currencyexchange.service;

import com.kodilla.currencyexchange.domain.User;
import com.kodilla.currencyexchange.exception.UserNotFoundException;
import com.kodilla.currencyexchange.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAllByActiveTrue();
    }

    public User getUserById(final Long id) throws UserNotFoundException {
        return userRepository.findByIdAndActiveTrue(id).orElseThrow(UserNotFoundException::new);
    }

    public User getUserByEmail(final String email) throws UserNotFoundException {
        return userRepository.findByEmailAddressAndActiveTrue(email).orElseThrow(UserNotFoundException::new);
    }

    public User saveUser(final User user) {
        return userRepository.save(user);
    }

    public void deleteUser(final Long id) throws UserNotFoundException {
        userRepository.delete(userRepository.findByIdAndActiveTrue(id).orElseThrow(UserNotFoundException::new));
    }

    public String generateApiKey(Long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        if (user.getApiKey() != null && user.getApiKeyExpiration() != null
                && LocalDateTime.now().isBefore(user.getApiKeyExpiration())) {
            return "API key has already been generated and is still valid.";
        }
        String newApiKey = UUID.randomUUID().toString();
        LocalDateTime expiration = LocalDateTime.now().plusHours(1);
        user.setApiKey(newApiKey);
        user.setApiKeyExpiration(expiration);
        userRepository.save(user);
        return "API key generated successfully and will expire in 1 hour.";
    }
}
