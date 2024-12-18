package com.example.backend.entity;

import lombok.Data;

@Data
public class Weather {
    private Long id;
    private String city;
    private String temperature;
    private String humidity; // 天气情况，如晴天、阴天等
    private String wind;
    private String date;
}