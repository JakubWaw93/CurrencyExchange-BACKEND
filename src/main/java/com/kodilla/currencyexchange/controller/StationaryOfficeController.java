package com.kodilla.currencyexchange.controller;

import com.kodilla.currencyexchange.domain.StationaryOfficeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/offices")
@RequiredArgsConstructor
public class StationaryOfficeController {

    @GetMapping
    public ResponseEntity<List<StationaryOfficeDto>> getAllOffices() {
        List<StationaryOfficeDto> stationaryOfficeDtoList = new ArrayList<>();
        return ResponseEntity.ok(stationaryOfficeDtoList);
    }

    @GetMapping("/{officeId}")
    public ResponseEntity<StationaryOfficeDto> getOfficeById(@PathVariable Long officeId) {
        StationaryOfficeDto stationaryOfficeDto = StationaryOfficeDto.builder()
                .id(officeId)
                .address("Jakiś address")
                .phone("123456798")
                .build();
        return ResponseEntity.ok(stationaryOfficeDto);
    }

    @PostMapping
    public ResponseEntity<StationaryOfficeDto> createOffice(@RequestBody StationaryOfficeDto stationaryOfficeDto) {
        return ResponseEntity.ok(stationaryOfficeDto);
    }

    @PutMapping
    public ResponseEntity<StationaryOfficeDto> updateOffice(@RequestBody StationaryOfficeDto stationaryOfficeDto) {
        return ResponseEntity.ok(stationaryOfficeDto);
    }

    @DeleteMapping("/{officeId}")
    public ResponseEntity<String> deleteOffice(@PathVariable String officeId) {
        return ResponseEntity.ok("Office with id: " + officeId + " was successfully deleted");
    }

}
