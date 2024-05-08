package com.kodilla.currencyexchange.controller;

import com.google.gson.Gson;
import com.kodilla.currencyexchange.domain.Currency;
import com.kodilla.currencyexchange.domain.CurrencyDto;
import com.kodilla.currencyexchange.domain.StationaryOffice;
import com.kodilla.currencyexchange.domain.StationaryOfficeDto;
import com.kodilla.currencyexchange.mapper.StationaryOfficeMapper;
import com.kodilla.currencyexchange.service.StationaryOfficeService;
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
@WebMvcTest(StationaryOfficeController.class)
@EntityScan("com.kodilla.currencyexchange.domain")
@ContextConfiguration(classes = {StationaryOfficeController.class, StationaryOfficeMapper.class})
public class StationaryOfficeControllerTestSuite {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StationaryOfficeService officeService;
    @MockBean
    private StationaryOfficeMapper officeMapper;

    @Test
    void shouldGetOffices() throws Exception {
        //Given
        List<StationaryOffice> offices = List.of(StationaryOffice.builder()
                .id(1L)
                .phone("111222333")
                .address("Address 1/2B")
                .build());
        List<StationaryOfficeDto> officeDtos = List.of(StationaryOfficeDto.builder()
                .id(1L)
                .phone("111222333")
                .address("Address 1/2B")
                .build());
        when(officeService.getAllOffices()).thenReturn(offices);
        when(officeMapper.mapToStationaryOfficeDtoList(offices)).thenReturn(officeDtos);
        //When&Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/offices")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].phone", Matchers.is("111222333")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].address", Matchers.is("Address 1/2B")));
    }

    @Test
    void shouldGetOffice() throws Exception {
        //Given
        StationaryOffice stationaryOffice = StationaryOffice.builder()
                .id(1L)
                .phone("111222333")
                .address("Address 1/2B")
                .build();
        StationaryOfficeDto stationaryOfficeDto = StationaryOfficeDto.builder()
                .id(1L)
                .phone("111222333")
                .address("Address 1/2B")
                .build();
        when(officeService.getOfficeById(any())).thenReturn(stationaryOffice);
        when(officeMapper.mapToStationaryOfficeDto(stationaryOffice)).thenReturn(stationaryOfficeDto);
        //When&Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/offices/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone", Matchers.is("111222333")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address", Matchers.is("Address 1/2B")));
    }

    @Test
    void shouldCreateOffice() throws Exception {
        //Given
        StationaryOfficeDto stationaryOfficeDto = StationaryOfficeDto.builder()
                .id(1L)
                .phone("111222333")
                .address("Address 1/2B")
                .build();
        Gson gson = new Gson();
        String jsonContent = gson.toJson(stationaryOfficeDto);
        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/offices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldUpdateOffice() throws Exception {
        //Given
        StationaryOffice stationaryOffice = StationaryOffice.builder()
                .id(1L)
                .phone("111222333")
                .address("Address 1/2B")
                .build();
        StationaryOfficeDto stationaryOfficeDto = StationaryOfficeDto.builder()
                .id(1L)
                .phone("111222333")
                .address("Address 1/2B")
                .build();

        when(officeMapper.mapToStationaryOffice(any(StationaryOfficeDto.class))).thenReturn(stationaryOffice);
        when(officeMapper.mapToStationaryOfficeDto(any(StationaryOffice.class))).thenReturn(stationaryOfficeDto);
        when(officeService.getOfficeById(1L)).thenReturn(stationaryOffice);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(stationaryOfficeDto);
        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/offices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone", Matchers.is("111222333")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address", Matchers.is("Address 1/2B")));
    }

    @Test
    void shouldDeleteOffice() throws Exception {
        // Given
        long officeId = 1;
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/offices/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
