package com.buffalo.ewaha.service;

import com.buffalo.ewaha.controller.dto.StationDto;
import com.buffalo.ewaha.controller.dto.StationMapDto;
import com.buffalo.ewaha.model.Station;
import com.buffalo.ewaha.repository.IStationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class StationService implements IStationService{

    @Autowired
    IStationRepository stationRepository;

    @Override
    @Transactional
    public List<StationMapDto> findAll() {
        List<Station> list = stationRepository.findAll();
        Map<String, Double> prices = stringToMap(list.get(0).getPrices());
        log.info("get all prices");
        return list.stream().map(this::mapToMapDto).collect(Collectors.toList());
    }

    @Override
    public void deleteStation(String localization) {
        Station station = stationRepository.findByLocalization(localization);
        stationRepository.delete(station);
    }

    private Map<String, Double> stringToMap(String pricesString){
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Double> pricesMap = new HashMap<>();
        if (pricesString != null) {
            try {
                String newPricesString = pricesString.replaceAll("'", "\"");
                pricesMap = mapper.readValue(newPricesString, Map.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return null;
        }
        return pricesMap;
    }

    private StationMapDto mapToMapDto(Station station){

        return StationMapDto.builder()
                .localization(station.getLocalization())
                .prices(stringToMap(station.getPrices()))
                .build();
    }

    private StationDto mapToDto(Station station){

        return StationDto.builder()
                .localization(station.getLocalization())
                .prices(station.getPrices())
                .build();
    }
}
