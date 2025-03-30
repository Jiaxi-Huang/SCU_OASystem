package com.example.backend.entity;

import lombok.Data;

@Data
public class Weather {
    private Long id;
    private String city;
    private String temperature;
    private String humidity; // 天气情况，如晴天、阴天等
    private String wind;
    private String win; // 风向
    private String win_meter; // 风速（km/h）
    private String visibility; // 能见度
    private String pressure; // 气压
    private String air; // 空气质量指数
    private String air_pm25; // PM2.5
    private String air_level; // 空气质量等级
    private String date;
}