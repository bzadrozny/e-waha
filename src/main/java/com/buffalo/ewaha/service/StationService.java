package com.buffalo.ewaha.service;

import com.buffalo.ewaha.controller.dto.StationDto;
import com.buffalo.ewaha.model.Station;
import com.buffalo.ewaha.repository.IStationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService implements IStationService{

    @Autowired
    IStationRepository stationRepository;

    @Override
    public List<StationDto> findAll() {
        List<Station> list = stationRepository.findAll();
        return list.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private StationDto mapToDto(Station station){

        return StationDto.builder()
                .localization(station.getLocalization())
                .prices(station.getPrices())
                .build();
    }
}
