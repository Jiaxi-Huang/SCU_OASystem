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
        LocalDate formattedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Weather weather = weatherService.getWeatherByCityAndDate(city, formattedDate);
        if (weather == null) {
            throw new RuntimeException("Weather data not found for city: " + city + " on date: " + formattedDate);
        }
        return weather;
    }
}