package com.kodilla.currencyexchangebackend.controller;

import com.kodilla.currencyexchangebackend.domain.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllActiveUsers() {
        List<UserDto> usersDto = new ArrayList<>();
        return ResponseEntity.ok(usersDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("userId") Long userId) {
        UserDto user =  UserDto.builder()
                .id(userId)
                .firstname("FirstName")
                .lastname("LastName")
                .emailAddress("some@email.com")
                .build();
        return ResponseEntity.ok(user);
    }

    @GetMapping("/email/{emailAddress}")
    private ResponseEntity<UserDto> getUserByEmail(@PathVariable String emailAddress) {
        UserDto user =  UserDto.builder()
                .id(1L)
                .firstname("FirstName")
                .lastname("LastName")
                .emailAddress(emailAddress)
                .build();
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userDto);
    }

    @PutMapping
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        return ResponseEntity.ok("User with id: " + userId + " was successfully deleted");
    }

    @PostMapping("/generateApiKey/{userId}")
    public ResponseEntity<String> generateNewApiKey(@PathVariable Long userId) {
        String apiKey = "sjd4suwgs4fu456wyegf564gd";
        return ResponseEntity.ok(apiKey);

    }
}
