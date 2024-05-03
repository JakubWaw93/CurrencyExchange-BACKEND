package com.kodilla.currencyexchange.domain;

import com.kodilla.currencyexchange.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
public class UserTestSuite {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void createUser() {
        user = User.builder()
                .firstname("Jan")
                .lastname("Kowalski")
                .login("JanKow")
                .password("1111")
                .emailAddress("JanKowalski@gmail.com")
                .apiKey("24987463541165498436516854")
                .build();
    }

    @BeforeEach
    public void cleanUpBefore() {
        userRepository.deleteAll();
    }
    @AfterEach
    public void cleanUp() {
        userRepository.deleteAll();
    }

    @Test
    void testSaveUserAndFindById() {
        //Given
        //When
        userRepository.save(user);
        Optional<User> retrievedUser = userRepository.findByIdAndActiveTrue(user.getId());
        //Then
        assertTrue(retrievedUser.isPresent());
        assertEquals("Kowalski", retrievedUser.get().getLastname());
    }

    @Test
    void testGetAllUsers() {
        //Given
        userRepository.save(user);
        //When
        List<User> users = userRepository.findAllByActiveTrue();
        //Then
        assertEquals(1, users.size());
        assertEquals(user.getId(), users.get(0).getId());
    }

    @Test
    void testDeleteUser() {
        //Given
        userRepository.save(user);
        //When
        userRepository.delete(user);
        List<User> users = userRepository.findAllByActiveTrue();
        List<User> allUsers = userRepository.findAll();
        //Then
        assertEquals(0, users.size());
        assertEquals(1, allUsers.size());
    }
}
