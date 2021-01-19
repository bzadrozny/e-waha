package com.buffalo.ewaha.controller;

import com.buffalo.ewaha.controller.dto.PhotoDto;
import com.buffalo.ewaha.controller.dto.StationDto;
import com.buffalo.ewaha.service.IPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class PhotoController {

    @Autowired
    private IPhotoService photoService;

    @PostMapping("/photo")
    public ResponseEntity<String> savePhoto(@RequestPart MultipartFile photo, @RequestParam String localization) throws IOException {
        PhotoDto photoDto = PhotoDto.builder()
                .photo(photo)
                .localization(localization)
                .build();
        photoService.savePhoto(photoDto);
        return new ResponseEntity<>("Photo uploaded", HttpStatus.CREATED);
    }
}
