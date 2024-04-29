package com.kodilla.currencyexchange.controller;

import com.kodilla.currencyexchange.domain.StationaryOffice;
import com.kodilla.currencyexchange.domain.StationaryOfficeDto;
import com.kodilla.currencyexchange.exception.StationaryOfficeNotFoundException;
import com.kodilla.currencyexchange.mapper.StationaryOfficeMapper;
import com.kodilla.currencyexchange.service.StationaryOfficeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/offices")
@RequiredArgsConstructor
public class StationaryOfficeController {

    private final StationaryOfficeService stationaryOfficeService;
    private final StationaryOfficeMapper stationaryOfficeMapper;

    @GetMapping
    public ResponseEntity<List<StationaryOfficeDto>> getAllOffices() {
        List<StationaryOffice> stationaryOfficeList = stationaryOfficeService.getAllOffices();
        return ResponseEntity.ok(stationaryOfficeMapper.mapToStationaryOfficeDtoList(stationaryOfficeList));
    }

    @GetMapping("/{officeId}")
    public ResponseEntity<StationaryOfficeDto> getOfficeById(@PathVariable Long officeId) throws StationaryOfficeNotFoundException {
        StationaryOffice stationaryOffice = stationaryOfficeService.getOfficeById(officeId);
        return ResponseEntity.ok(stationaryOfficeMapper.mapToStationaryOfficeDto(stationaryOffice));
    }

    @PostMapping
    public ResponseEntity<Void> createOffice(@RequestBody StationaryOfficeDto stationaryOfficeDto) {
        StationaryOffice stationaryOffice = stationaryOfficeMapper.mapToStationaryOffice(stationaryOfficeDto);
        stationaryOfficeService.saveStationaryOffice(stationaryOffice);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<StationaryOfficeDto> updateOffice(@RequestBody StationaryOfficeDto stationaryOfficeDto) {
        StationaryOffice stationaryOffice = stationaryOfficeMapper.mapToStationaryOffice(stationaryOfficeDto);
        StationaryOffice savedStationaryOffice = stationaryOfficeService.saveStationaryOffice(stationaryOffice);
        return ResponseEntity.ok(stationaryOfficeMapper.mapToStationaryOfficeDto(savedStationaryOffice));
    }

    @DeleteMapping("/{officeId}")
    public ResponseEntity<Void> deleteOffice(@PathVariable Long officeId) throws StationaryOfficeNotFoundException {
        stationaryOfficeService.deleteStationaryOffice(officeId);
        return ResponseEntity.ok().build();
    }

}
