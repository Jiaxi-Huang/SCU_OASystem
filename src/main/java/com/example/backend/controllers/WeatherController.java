package com.example.backend.controllers;

import com.example.backend.entity.Weather;
import com.example.backend.services.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {
    @Autowired
    private WeatherService weatherService;

    @GetMapping
    public Weather getWeather(@RequestParam String city, @RequestParam String date) {
        System.out.println("Received city parameter: " + city); // 输出接收到的城市参数
        System.out.println("Received date parameter: " + date); // 输出接收到的日期参数
        LocalDate formattedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Weather weather = weatherService.getWeatherByCityAndDate(city, formattedDate);
        if (weather == null) {
            throw new RuntimeException("Weather data not found for city: " + city + " on date: " + formattedDate);
        }
        System.out.println("Weather data from database for city " + city + " on date " + formattedDate + ": " + weather);
        return weather;
    }
}
