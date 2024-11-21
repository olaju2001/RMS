package com.kisters.model;

import lombok.Data;

@Data
public class WeatherData {
    private Double temperature;
    private Double windSpeed;
    private Integer cloudCover;
    private Double solarRadiation;
    private String conditions;
    private Double precipitation;
}