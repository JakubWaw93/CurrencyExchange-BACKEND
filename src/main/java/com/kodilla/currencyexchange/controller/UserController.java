package com.kodilla.currencyexchange.controller;

import com.kodilla.currencyexchange.domain.User;
import com.kodilla.currencyexchange.domain.UserDto;
import com.kodilla.currencyexchange.exception.FailedToGenerateApiKeyException;
import com.kodilla.currencyexchange.exception.UserNotFoundException;
import com.kodilla.currencyexchange.mapper.UserMapper;
import com.kodilla.currencyexchange.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllActiveUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(userMapper.mapToUserDtoList(users));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) throws UserNotFoundException {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(userMapper.mapToUserDto(user));
    }

    @GetMapping("/email/{emailAddress}")
    private ResponseEntity<UserDto> getUserByEmail(@PathVariable String emailAddress) throws UserNotFoundException {
        User user = userService.getUserByEmail(emailAddress);
        return ResponseEntity.ok(userMapper.mapToUserDto(user));
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody UserDto userDto) {
        User user = userMapper.mapToUser(userDto);
        userService.saveUser(user);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto) {
        User user = userMapper.mapToUser(userDto);
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(userMapper.mapToUserDto(savedUser));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) throws UserNotFoundException {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/generateApiKey/{userId}")
    public ResponseEntity<String> generateNewApiKey(@PathVariable Long userId) throws FailedToGenerateApiKeyException {
        try {
            String apiKey = userService.generateApiKey(userId);
            return ResponseEntity.ok("API key generated successfully: " + apiKey);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            throw new FailedToGenerateApiKeyException();
        }
    }
}
