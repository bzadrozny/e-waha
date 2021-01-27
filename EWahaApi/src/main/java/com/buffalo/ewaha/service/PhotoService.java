package com.buffalo.ewaha.service;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.specialized.BlockBlobClient;
import com.buffalo.ewaha.commons.NotAcceptableException;
import com.buffalo.ewaha.commons.PayloadTooLargeException;
import com.buffalo.ewaha.commons.UnprocessableEntityException;
import com.buffalo.ewaha.controller.dto.PhotoDto;
import com.buffalo.ewaha.model.Station;
import com.buffalo.ewaha.repository.IStationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@Transactional
public class PhotoService implements IPhotoService{

    @Autowired
    private IStationRepository stationRepository;

    @Override
    @Transactional
    public void savePhoto(PhotoDto photoDto) throws IOException, UnprocessableEntityException, NotAcceptableException, PayloadTooLargeException {
        //TODO send to blobstorage
        if (!photoDto.getLocalization().matches("^[0-9]{1,3}[.][0-9]*_[0-9]{1,3}[.][0-9]*")){
            log.warn("Wrong localization");
            throw new UnprocessableEntityException("Wrong localization syntax");
        }
        if (photoDto.getPhoto().isEmpty() || photoDto.getPhoto() == null){
            log.warn("Request without photo file");
            throw new NotAcceptableException("Photo Required");
        } else if (photoDto.getPhoto().getSize() > 15000000){
            log.warn("Request photo file is too large");
            throw new PayloadTooLargeException("File is too large (max. 15MB)");
        }
        Station station = stationRepository.findByLocalization(photoDto.getLocalization());
        if (station == null){
            //TODO send to blobstorage
            station = Station.builder()
                    .localization(photoDto.getLocalization())
                    .build();
            stationRepository.save(station);
            log.info("saved location in db");
        }
            //TODO send to blobstorage
        String yourSasToken = "?sv=2019-12-12&ss=b&srt=sco&sp=rwdlacx&se=2021-01-30T21:49:58Z&st=2021-01-23T13:49:58Z&spr=https,http&sig=G6PXQgOe3ZHCMKUY7yeOcK7JCPrMyFahYKqEPoj9oe4%3D";
        /* Create a new BlobServiceClient with a SAS Token */
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .endpoint("https://ewahastorage.blob.core.windows.net/blob")
                .sasToken(yourSasToken)
                .buildClient();
        log.info("Connected to blobService " + blobServiceClient.getAccountInfo());
        /* Create a new container client */
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient("blob");
        log.info("Connected to container: " + containerClient.getBlobContainerName());

        /* Upload the file to the container */
        System.out.println(containerClient.getBlobContainerUrl());
        BlockBlobClient blockBlobClient = containerClient.getBlobClient(UUID.randomUUID().toString()).getBlockBlobClient();
        String url = "";
        try (ByteArrayInputStream dataStream = new ByteArrayInputStream(photoDto.getPhoto().getBytes())) {

            blockBlobClient.upload(dataStream, photoDto.getPhoto().getSize());
            url = blockBlobClient.getBlobUrl();
            log.info("uploaded file to blob storage and return url: " + url);
        }
        sendUrlToApi2(photoDto.getLocalization(), url);
//        System.out.println("req: " + sendUrlToApi2(photoDto.getLocalization(), url));
    }

    private String sendUrlToApi2(String localization, String url){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        String uri = "http://verysmartapi?loc=" + localization + "&photo_url=" + url;
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.POST,
                entity,
                String.class
        );
        log.info("sended request to verysmartapi");
        return responseEntity.getStatusCode().toString();
    }
}
