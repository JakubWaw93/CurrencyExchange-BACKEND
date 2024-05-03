package com.kodilla.currencyexchange.service;

import com.kodilla.currencyexchange.domain.User;
import com.kodilla.currencyexchange.exception.UserNotFoundException;
import com.kodilla.currencyexchange.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

@SpringBootTest
@Transactional
public class UserServiceTestSuite {

    @Autowired
    private UserRepository userRepository;
    @Autowired UserService userService;

    @AfterEach
    void cleanUp() {
        userRepository.deleteAll();
    }

    @Test
    void testGenerateApiKey() throws UserNotFoundException {
        User userWithActiveApiKey = User.builder()
                .firstname("Jan")
                .lastname("Kowalski")
                .emailAddress("jan@kowalski.com")
                .apiKey("1234567890")
                .login("JanKow")
                .password("1111")
                .apiKeyExpiration(LocalDateTime.now().plusHours(1))
                .build();

        User userWithExpiredApiKey = User.builder()
                .firstname("Andrzej")
                .lastname("Nowak")
                .login("AndNow")
                .password("2222")
                .emailAddress("Andrzej@Nowak.com")
                .apiKey("2384675287635")
                .apiKeyExpiration(LocalDateTime.now().minusMinutes(30))
                .build();

        User userWithoutApiKey = User.builder()
                .firstname("Piotr")
                .lastname("Paweł")
                .login("PioPał")
                .password("1234")
                .emailAddress("piotr@paweł.com")
                .build();

        userRepository.save(userWithActiveApiKey);
        userRepository.save(userWithExpiredApiKey);
        userRepository.save(userWithoutApiKey);

        //When
        String forUserWithValidKey = userService.generateApiKey(userWithActiveApiKey.getId());
        String forUserWithInvalidKey = userService.generateApiKey(userWithExpiredApiKey.getId());
        String forUSerWithoutKey = userService.generateApiKey(userWithoutApiKey.getId());

        //Then
        assertEquals("API key has already been generated and is still valid.", forUserWithValidKey);
        assertEquals("API key generated successfully and will expire in 1 hour.", forUserWithInvalidKey);
        assertEquals("API key generated successfully and will expire in 1 hour.", forUSerWithoutKey);
        assertEquals("1234567890", userWithActiveApiKey.getApiKey());
        assertNotEquals("2384675287635", userWithActiveApiKey.getApiKey());
        assertNotNull(userWithActiveApiKey.getApiKey());
        assertTrue(LocalDateTime.now().isBefore(userWithActiveApiKey.getApiKeyExpiration()));
        assertTrue(LocalDateTime.now().isBefore(userWithExpiredApiKey.getApiKeyExpiration()));
        assertTrue(LocalDateTime.now().isBefore(userWithoutApiKey.getApiKeyExpiration()));
        assertFalse(LocalDateTime.now().plusHours(1).isBefore(userWithoutApiKey.getApiKeyExpiration()));
    }
}
