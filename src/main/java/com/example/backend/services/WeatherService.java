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
//        System.out.println("Querying weather data for city: " + city + " on date: " + formattedDate); // 输出查询信息
        Weather weather = weatherMapper.findByCityAndDate(city, formattedDate);
        if (weather != null) {
//            System.out.println("Fetched weather data: " + weather);
            return weather;
        } else {
//            System.out.println("No weather data found for city: " + city + " on date: " + formattedDate);
            return null;
        }
    }
}