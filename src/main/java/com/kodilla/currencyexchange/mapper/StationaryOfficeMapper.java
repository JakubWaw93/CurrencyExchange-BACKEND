package com.kodilla.currencyexchange.mapper;

import com.kodilla.currencyexchange.domain.StationaryOffice;
import com.kodilla.currencyexchange.domain.StationaryOfficeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StationaryOfficeMapper {

    public StationaryOffice mapToStationaryOffice(final StationaryOfficeDto stationaryOfficeDto) {
        return StationaryOffice.builder()
                .id(stationaryOfficeDto.getId())
                .address(stationaryOfficeDto.getAddress())
                .phone(stationaryOfficeDto.getPhone())
                .active(stationaryOfficeDto.isActive())
                .build();
    }

    public StationaryOfficeDto mapToStationaryOfficeDto(final StationaryOffice stationaryOffice) {
        return StationaryOfficeDto.builder()
                .id(stationaryOffice.getId())
                .address(stationaryOffice.getAddress())
                .phone(stationaryOffice.getPhone())
                .active(stationaryOffice.isActive())
                .build();
    }

    public List<StationaryOfficeDto> mapToStationaryOfficeDtoList(final List<StationaryOffice> stationaryOfficeList) {
        return stationaryOfficeList.stream()
                .map(this::mapToStationaryOfficeDto)
                .toList();
    }
}
