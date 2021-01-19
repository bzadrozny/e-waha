package com.buffalo.ewaha.service;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.specialized.BlockBlobClient;
import com.buffalo.ewaha.controller.dto.PhotoDto;
import com.buffalo.ewaha.model.Station;
import com.buffalo.ewaha.repository.IStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class PhotoService implements IPhotoService{

    @Autowired
    private IStationRepository stationRepository;

    @Override
    public void savePhoto(PhotoDto photoDto) throws IOException {
        //TODO send to blobstorage
        Station station = stationRepository.findByLocalization(photoDto.getLocalization());
        if (station == null){
            //TODO send to blobstorage
            station = Station.builder()
                    .localization(photoDto.getLocalization())
                    .build();
            stationRepository.save(station);
        }
            //TODO send to blobstorage
        System.out.println("Start blob");
        String yourSasToken = "?sv=2019-12-12&ss=b&srt=sco&sp=rwdlacx&se=2021-01-22T23:57:15Z&st=2021-01-18T15:57:15Z&spr=https,http&sig=XSV0gOkohJn6na0x%2FdNACmVwlHX80OWabyL2zZ9%2F5s0%3D";
        /* Create a new BlobServiceClient with a SAS Token */
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .endpoint("https://ewahastorage.blob.core.windows.net/blob")
                .sasToken(yourSasToken)
                .buildClient();
        System.out.println("Connected to service: " + blobServiceClient.getAccountInfo());
        /* Create a new container client */
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient("blob");
        System.out.println("Connected to container: " + containerClient.getBlobContainerName());

        /* Upload the file to the container */
        System.out.println(containerClient.getBlobContainerUrl());
        BlockBlobClient blockBlobClient = containerClient.getBlobClient(UUID.randomUUID().toString()).getBlockBlobClient();
        String dataSample = "samples";
        System.out.println("account info: " + blobServiceClient.getAccountInfo());
        String url = "";
        try (ByteArrayInputStream dataStream = new ByteArrayInputStream(photoDto.getPhoto().getBytes())) {

            blockBlobClient.upload(dataStream, photoDto.getPhoto().getSize());
            url = blockBlobClient.getBlobUrl();
        }
//        blobClient.uploadFromFile("../../resources/images.png");
//        blobClient.upload(photoDto.getPhoto().getInputStream(), photoDto.getPhoto().getSize());
        System.out.println("uploaded file: " + url);
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
        System.out.println("Test");
        return responseEntity.getStatusCode().toString();
    }
}
