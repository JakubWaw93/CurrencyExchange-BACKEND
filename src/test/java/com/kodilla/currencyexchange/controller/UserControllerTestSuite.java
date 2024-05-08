package com.kodilla.currencyexchange.controller;

import com.google.gson.Gson;
import com.kodilla.currencyexchange.domain.User;
import com.kodilla.currencyexchange.domain.UserDto;
import com.kodilla.currencyexchange.mapper.UserMapper;
import com.kodilla.currencyexchange.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(UserController.class)
@EntityScan("com.kodilla.currencyexchange.domain")
@ContextConfiguration(classes = {UserController.class, UserMapper.class})
public class UserControllerTestSuite {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private UserMapper userMapper;

    @Test
    void shouldGetUsers() throws Exception {
        //Given
        List<User> users = List.of(User.builder()
                .id(1L)
                .firstname("Jan")
                .lastname("Nowak")
                .password("password")
                .login("JaNow")
                .emailAddress("JanNowak@gmail.com")
                .build());
        List<UserDto> userDtos = List.of(UserDto.builder()
                .id(1L)
                .firstname("Jan")
                .lastname("Nowak")
                .password("password")
                .login("JaNow")
                .emailAddress("JanNowak@gmail.com")
                .build());
        when(userService.getAllUsers()).thenReturn(users);
        when(userMapper.mapToUserDtoList(users)).thenReturn(userDtos);
        //When&Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].login", Matchers.is("JaNow")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstname", Matchers.is("Jan")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastname", Matchers.is("Nowak")));
    }

    @Test
    void shouldGetUser() throws Exception {
        //Given
        User user = User.builder()
                .id(1L)
                .firstname("Jan")
                .lastname("Nowak")
                .password("password")
                .login("JaNow")
                .emailAddress("JanNowak@gmail.com")
                .build();
        UserDto userDto = UserDto.builder()
                .id(1L)
                .firstname("Jan")
                .lastname("Nowak")
                .password("password")
                .login("JaNow")
                .emailAddress("JanNowak@gmail.com")
                .build();
        when(userService.getUserById(any())).thenReturn(user);
        when(userService.getUserByEmail(any())).thenReturn(user);
        when(userMapper.mapToUserDto(user)).thenReturn(userDto);
        //When&Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstname", Matchers.is("Jan")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastname", Matchers.is("Nowak")));

        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/users/email/JanNowak@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstname", Matchers.is("Jan")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastname", Matchers.is("Nowak")));
    }

    @Test
    void shouldCreateUser() throws Exception {
        //Given
        UserDto userDto = UserDto.builder()
                .id(1L)
                .firstname("Jan")
                .lastname("Nowak")
                .password("password")
                .login("JaNow")
                .emailAddress("JanNowak@gmail.com")
                .build();
        Gson gson = new Gson();
        String jsonContent = gson.toJson(userDto);
        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void shouldUpdateUser() throws Exception {
        //Given
        User user = User.builder()
                .id(1L)
                .firstname("Jan")
                .lastname("Nowak")
                .password("password")
                .login("JaNow")
                .emailAddress("JanNowak@gmail.com")
                .build();
        UserDto userDto = UserDto.builder()
                .id(1L)
                .firstname("Jan")
                .lastname("Nowak")
                .password("password")
                .login("JaNow")
                .emailAddress("JanNowak@gmail.com")
                .build();

        when(userMapper.mapToUser(any(UserDto.class))).thenReturn(user);
        when(userMapper.mapToUserDto(any(User.class))).thenReturn(userDto);
        when(userService.getUserById(1L)).thenReturn(user);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(userDto);
        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstname", Matchers.is("Jan")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastname", Matchers.is("Nowak")));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        // Given
        long userId = 1;
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
