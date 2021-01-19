package com.buffalo.ewaha.service;

import com.buffalo.ewaha.controller.dto.StationDto;

import java.util.List;

public interface IStationService {
    List<StationDto> findAll();
}
