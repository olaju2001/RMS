package com.kisters.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${external.weather.api.base-url}")
    private String baseUrl;

    @Value("${external.weather.api.key}")
    private String apiKey;

    @Bean
    public WebClient weatherWebClient() {  // Changed from weatherApiClient to weatherWebClient
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("X-API-Key", apiKey)
                .build();
    }
}