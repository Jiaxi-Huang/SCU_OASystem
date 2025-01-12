package com.example.backend.services;

import com.example.backend.entity.Weather;
import com.example.backend.mapper.WeatherMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class WeatherService {
    @Autowired
    private WeatherMapper weatherMapper;

    public Weather getWeatherByCityAndDate(String city, LocalDate date) {
        String formattedDate = date.toString(); // 使用标准的日期格式 2024-12-18
        Weather weather = weatherMapper.findByCityAndDate(city, formattedDate);
        if (weather != null) {
            return weather;
        } else {
            return null;
        }
    }
}