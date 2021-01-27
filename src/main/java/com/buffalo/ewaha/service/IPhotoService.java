package com.buffalo.ewaha.service;

import com.buffalo.ewaha.commons.NotAcceptableException;
import com.buffalo.ewaha.commons.PayloadTooLargeException;
import com.buffalo.ewaha.commons.UnprocessableEntityException;
import com.buffalo.ewaha.controller.dto.PhotoDto;

import java.io.IOException;

public interface IPhotoService {

    void savePhoto(PhotoDto photoDto) throws IOException, UnprocessableEntityException, NotAcceptableException, PayloadTooLargeException;
}
