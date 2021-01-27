package com.buffalo.ewaha.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StationMapDto {
    private String localization;
    private Map<String, Double> prices = new HashMap<>();
}
