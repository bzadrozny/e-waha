package com.buffalo.ewaha.controller;

import com.buffalo.ewaha.controller.dto.PhotoDto;
import com.buffalo.ewaha.controller.dto.StationDto;
import com.buffalo.ewaha.controller.dto.StationMapDto;
import com.buffalo.ewaha.service.IPhotoService;
import com.buffalo.ewaha.service.IStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class StationController {

    @Autowired
    private IStationService stationService;

    @GetMapping("/prices")
    public ResponseEntity<List<StationMapDto>> getPrices(){
        return new ResponseEntity<>(stationService.findAll(), HttpStatus.OK);
    }

    @DeleteMapping("/prices/{localization}")
    public ResponseEntity<String> deleteStation(@PathVariable String localization){
        stationService.deleteStation(localization);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
