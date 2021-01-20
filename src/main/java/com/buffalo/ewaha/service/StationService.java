package com.buffalo.ewaha.service;

import com.buffalo.ewaha.controller.dto.StationDto;
import com.buffalo.ewaha.model.Station;
import com.buffalo.ewaha.repository.IStationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class StationService implements IStationService{

    @Autowired
    IStationRepository stationRepository;

    @Override
    @Transactional
    public List<StationDto> findAll() {
        List<Station> list = stationRepository.findAll();
        log.info("get all prices");
        return list.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private StationDto mapToDto(Station station){

        return StationDto.builder()
                .localization(station.getLocalization())
                .prices(station.getPrices())
                .build();
    }
}
