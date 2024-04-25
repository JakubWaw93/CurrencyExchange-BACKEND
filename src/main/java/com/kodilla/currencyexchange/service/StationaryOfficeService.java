package com.kodilla.currencyexchange.service;

import com.kodilla.currencyexchange.domain.StationaryOffice;
import com.kodilla.currencyexchange.exception.StationaryOfficeNotFoundException;
import com.kodilla.currencyexchange.repository.StationaryOfficeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StationaryOfficeService {

    private final StationaryOfficeRepository stationaryOfficeRepository;

    public List<StationaryOffice> getAllOffices() {
        return stationaryOfficeRepository.findAllByActiveTrue();
    }

    public StationaryOffice getOfficeById(final Long id) {
        return stationaryOfficeRepository.findByIdAndActiveTrue(id).orElseThrow(StationaryOfficeNotFoundException::new);
    }

    public StationaryOffice saveStationaryOffice(final StationaryOffice stationaryOffice) {
        return stationaryOfficeRepository.save(stationaryOffice);
    }

    public void deleteStationaryOffice(final Long id) {
        stationaryOfficeRepository.delete(stationaryOfficeRepository.findByIdAndActiveTrue(id).orElseThrow(StationaryOfficeNotFoundException::new));
    }
}
