package com.kodilla.currencyexchange.mapper;

import com.kodilla.currencyexchange.domain.User;
import com.kodilla.currencyexchange.domain.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class UserMapperTestSuite {


    @Autowired
    private UserMapper userMapper;


    @Test
    void testMapToUser() {
        //Given
        UserDto userDto = UserDto.builder()
                .id(1L)
                .apiKey("123456789")
                .emailAddress("something@gmail.com")
                .firstname("Jan")
                .lastname("Kowalski")
                .build();
        //When
        User user = userMapper.mapToUser(userDto);
        //Then
        assertEquals(1L, user.getId());
        assertEquals("123456789", user.getApiKey());
        assertEquals("something@gmail.com", user.getEmailAddress());
        assertEquals("Kowalski", user.getLastname());

    }

    @Test
    void testMapToUserDto() {
        //Given
        User user = User.builder()
                .id(1L)
                .apiKey("123456789")
                .emailAddress("something@gmail.com")
                .firstname("Jan")
                .lastname("Kowalski")
                .build();
        //When
        UserDto userDto = userMapper.mapToUserDto(user);
        //Then
        assertEquals(1L, userDto.getId());
        assertEquals("123456789", userDto.getApiKey());
        assertEquals("something@gmail.com", userDto.getEmailAddress());
        assertEquals("Kowalski", userDto.getLastname());
        assertEquals(0, userDto.getTransactionsIds().size());
    }

    @Test
    void testMapToUserDtoList() {
        //Given
        User user1 = User.builder()
                .id(1L)
                .apiKey("123456789")
                .emailAddress("something@gmail.com")
                .firstname("Jan")
                .lastname("Kowalski")
                .build();

        User user2 = User.builder()
                .id(2L)
                .apiKey("123456789")
                .emailAddress("something@gmail.com")
                .firstname("Andrzej")
                .lastname("Kowalski")
                .build();

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        //When
        List<UserDto> userDtoList = userMapper.mapToUserDtoList(users);
        //Then
        assertEquals(2, userDtoList.size());
        assertEquals(2L, userDtoList.get(1).getId());
        assertEquals("123456789", userDtoList.get(1).getApiKey());
        assertEquals("something@gmail.com", userDtoList.get(1).getEmailAddress());
        assertEquals("Kowalski", userDtoList.get(1).getLastname());
        assertEquals(0, userDtoList.get(1).getTransactionsIds().size());
    }


}
