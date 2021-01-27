package com.buffalo.ewaha.service;

import com.buffalo.ewaha.controller.dto.StationDto;
import com.buffalo.ewaha.controller.dto.StationMapDto;

import java.util.List;

public interface IStationService {
    List<StationMapDto> findAll();
    void deleteStation(String localization);
}
