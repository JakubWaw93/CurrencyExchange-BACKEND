package com.kodilla.currencyexchange.mapper;

import com.kodilla.currencyexchange.domain.StationaryOffice;
import com.kodilla.currencyexchange.domain.StationaryOfficeDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class StationaryOfficeMapperTestSuite {

    @InjectMocks
    private StationaryOfficeMapper stationaryOfficeMapper;

    @Test
    void testMapToStationaryOffice() {
        //Given
        StationaryOfficeDto stationaryOfficeDto = StationaryOfficeDto.builder()
                .id(1L)
                .phone("333444666")
                .address("Nice place 888")
                .build();
        //When
        StationaryOffice stationaryOffice = stationaryOfficeMapper.mapToStationaryOffice(stationaryOfficeDto);
        //Then
        assertEquals(1L, stationaryOffice.getId());
        assertEquals("333444666", stationaryOffice.getPhone());
        assertEquals("Nice place 888", stationaryOffice.getAddress());
    }

    @Test
    void testMapToStationaryOfficeDto() {
        //Given
        StationaryOffice stationaryOffice = StationaryOffice.builder()
                .id(1L)
                .phone("333444666")
                .address("Nice place 888")
                .build();
        //When
        StationaryOfficeDto stationaryOfficeDto = stationaryOfficeMapper.mapToStationaryOfficeDto(stationaryOffice);
        //Then
        assertEquals(1L, stationaryOfficeDto.getId());
        assertEquals("333444666", stationaryOfficeDto.getPhone());
        assertEquals("Nice place 888", stationaryOfficeDto.getAddress());
    }

    @Test
    void testMapToStationaryOfficeDtoList() {
        //Given
        StationaryOffice stationaryOffice1 = StationaryOffice.builder()
                .id(1L)
                .phone("333444666")
                .address("Nice place 888")
                .build();

        StationaryOffice stationaryOffice2 = StationaryOffice.builder()
                .id(2L)
                .phone("333888666")
                .address("Ugly place 888")
                .build();

        List<StationaryOffice> stationaryOfficeList = new ArrayList<>();
        stationaryOfficeList.add(stationaryOffice1);
        stationaryOfficeList.add(stationaryOffice2);
        //When
        List<StationaryOfficeDto> stationaryOfficeDtoList = stationaryOfficeMapper.mapToStationaryOfficeDtoList(stationaryOfficeList);
        //Then
        assertEquals(2, stationaryOfficeDtoList.size());
        assertEquals(1L, stationaryOfficeDtoList.get(0).getId());
        assertEquals("333444666", stationaryOfficeDtoList.get(0).getPhone());
        assertEquals("Nice place 888", stationaryOfficeDtoList.get(0).getAddress());
    }
}
