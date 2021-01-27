package com.buffalo.ewaha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import com.azure.storage.blob.*;
import com.azure.storage.blob.models.*;
import java.io.*;

@SpringBootApplication
@EnableSwagger2
public class EWahaApplication {

    public static void main(String[] args) {
        SpringApplication.run(EWahaApplication.class, args);
    }

}
