package com.kisters.client;

import com.kisters.model.WeatherData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
@Slf4j
@RequiredArgsConstructor
public class WeatherApiClient {

    private final WebClient weatherWebClient;  // Changed from weatherApiClient to weatherWebClient

    @Value("${external.weather.api.timeout}")
    private Integer timeout;

    public Mono<WeatherData> getWeatherData(String latitude, String longitude) {
        return weatherWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/current")
                        .queryParam("lat", latitude)
                        .queryParam("lon", longitude)
                        .build())
                .retrieve()
                .bodyToMono(WeatherData.class)
                .timeout(Duration.ofSeconds(timeout))
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(2)))
                .doOnError(error -> log.error("Error fetching weather data: {}", error.getMessage()))
                .onErrorResume(error -> Mono.empty());
    }

    public Mono<WeatherData> getForecast(String latitude, String longitude) {
        return weatherWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/forecast")
                        .queryParam("lat", latitude)
                        .queryParam("lon", longitude)
                        .build())
                .retrieve()
                .bodyToMono(WeatherData.class)
                .timeout(Duration.ofSeconds(timeout))
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(2)))
                .doOnError(error -> log.error("Error fetching weather forecast: {}", error.getMessage()))
                .onErrorResume(error -> Mono.empty());
    }
}