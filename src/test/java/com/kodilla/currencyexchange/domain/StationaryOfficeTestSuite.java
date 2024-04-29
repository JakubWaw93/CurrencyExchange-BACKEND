package com.kodilla.currencyexchange.domain;

import com.kodilla.currencyexchange.repository.StationaryOfficeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class StationaryOfficeTestSuite {

    @Autowired
    private StationaryOfficeRepository stationaryOfficeRepository;

    private StationaryOffice stationaryOffice;

    @BeforeEach
    void createStationaryOffice() {
        stationaryOffice = StationaryOffice.builder()
                .phone("012345678")
                .address("Plac Defilad 1")
                .build();
    }

    @BeforeEach
    public void cleanUpBefore() {
        stationaryOfficeRepository.deleteAll();
    }
    @AfterEach
    public void cleanUp() {
        stationaryOfficeRepository.deleteAll();
    }

    @Test
    void testSaveStationaryOfficeAndFindById() {
        //Given
        //When
        stationaryOfficeRepository.save(stationaryOffice);
        Optional<StationaryOffice> retrievedStationaryOffice = stationaryOfficeRepository.findByIdAndActiveTrue(stationaryOffice.getId());
        //Then
        assertTrue(retrievedStationaryOffice.isPresent());
        assertEquals("012345678", retrievedStationaryOffice.get().getPhone());
    }

    @Test
    void testGetAllStationaryOffices() {
        //Given
        stationaryOfficeRepository.save(stationaryOffice);
        //When
        List<StationaryOffice> stationaryOffices = stationaryOfficeRepository.findAllByActiveTrue();
        //Then
        assertEquals(1, stationaryOffices.size());
        assertEquals(stationaryOffice.getId(), stationaryOffices.get(0).getId());
    }

    @Test
    void testDeleteStationaryOffice() {
        //Given
        stationaryOfficeRepository.save(stationaryOffice);
        //When
        stationaryOfficeRepository.delete(stationaryOffice);
        List<StationaryOffice> stationaryOffices = stationaryOfficeRepository.findAllByActiveTrue();
        List<StationaryOffice> allStationaryOffices = stationaryOfficeRepository.findAll();
        //Then
        assertEquals(0, stationaryOffices.size());
        assertEquals(1, allStationaryOffices.size());
    }
}
