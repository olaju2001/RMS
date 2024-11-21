package com.kisters.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public <T> void sendMessage(String topic, T message) {
        try {
            String messageJson = objectMapper.writeValueAsString(message);
            kafkaTemplate.send(topic, messageJson)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            log.info("Message sent successfully to topic: {}", topic);
                        } else {
                            log.error("Failed to send message to topic: {}", topic, ex);
                        }
                    });
        } catch (Exception e) {
            log.error("Error serializing message", e);
            throw new RuntimeException("Error sending message to Kafka", e);
        }
    }
}