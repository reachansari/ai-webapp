package com.example.rag.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Service
public class WeatherService {

    @Value("${OPENWEATHER_API_KEY}")
    private String apiKey;

    private final WebClient webClient = WebClient.builder().baseUrl("https://api.openweathermap.org/data/2.5").build();

    public Map<String, Object> getWeatherForCity(String city) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/weather")
                        .queryParam("q", city)
                        .queryParam("appid", apiKey)
                        .queryParam("units", "metric")
                        .build())
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    public Map<String, Map<String, Object>> getWeatherForCities(String citiesCsv) {
        Map<String, Map<String, Object>> result = new HashMap<>();
        String[] cities = citiesCsv.split(",");
        for (String city : cities) {
            city = city.trim();
            result.put(city, getWeatherForCity(city));
        }
        return result;
    }
}
