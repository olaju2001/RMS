package com.kisters.service;


import com.kisters.client.WeatherApiClient;
import com.kisters.model.Resource;
import com.kisters.model.WeatherData;
import com.kisters.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class WeatherMonitoringService {

    private final WeatherApiClient weatherApiClient;
    private final ResourceRepository resourceRepository;
    private final KafkaProducerService kafkaProducer;

    @Scheduled(fixedRate = 900000) // Every 15 minutes
    public void updateWeatherData() {
        List<Resource> resources = resourceRepository.findAll();

        Flux.fromIterable(resources)
                .flatMap(resource -> fetchAndProcessWeatherData(resource))
                .doOnComplete(() -> log.info("Weather data update completed"))
                .doOnError(error -> log.error("Error updating weather data", error))
                .subscribe();
    }

    private Mono<Void> fetchAndProcessWeatherData(Resource resource) {
        return weatherApiClient.getWeatherData(
                        resource.getLocation().getLatitude(),
                        resource.getLocation().getLongitude()
                )
                .doOnNext(weatherData -> processWeatherData(resource, weatherData))
                .then();
    }

    private void processWeatherData(Resource resource, WeatherData weatherData) {
        try {
            Map<String, Object> weatherUpdate = new HashMap<>();
            weatherUpdate.put("resourceId", resource.getId());
            weatherUpdate.put("weatherData", weatherData);

            kafkaProducer.sendMessage("weather-updates", weatherUpdate);
        } catch (Exception e) {
            log.error("Error processing weather data for resource {}", resource.getId(), e);
        }
    }
}