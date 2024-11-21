package com.kisters.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kisters.dto.MonitoringMetricDTO;
import com.kisters.model.ProcessedData;
import com.kisters.model.Resource;
import com.kisters.repository.ProcessedDataRepository;
import com.kisters.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataProcessingService {

    private final ProcessedDataRepository processedDataRepository;
    private final ResourceRepository resourceRepository;
    private final ObjectMapper objectMapper;
    private final KafkaProducerService kafkaProducer;

    @KafkaListener(topics = "resource-metrics", groupId = "resource-management-group")
    @Transactional
    public void processMetricData(String message) {
        try {
            MonitoringMetricDTO metric = objectMapper.readValue(message, MonitoringMetricDTO.class);
            Resource resource = resourceRepository.findById(metric.getResourceId())
                    .orElseThrow(() -> new RuntimeException("Resource not found"));

            ProcessedData processedData = new ProcessedData();
            processedData.setResource(resource);
            processedData.setTimestamp(metric.getTimestamp());
            processedData.setDataType("EFFICIENCY");
            processedData.setProcessedValue(calculateEfficiency(metric));

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("originalMetricId", metric.getId());
            metadata.put("weatherConditions", metric.getWeatherConditions());
            processedData.setMetadata(objectMapper.writeValueAsString(metadata));

            processedDataRepository.save(processedData);

            // Send processed data to analytics topic
            kafkaProducer.sendMessage("processed-metrics", processedData);

            log.info("Successfully processed metric data for resource: {}", metric.getResourceId());
        } catch (Exception e) {
            log.error("Error processing metric data", e);
            throw new RuntimeException("Error processing metric data", e);
        }
    }

    private Double calculateEfficiency(MonitoringMetricDTO metric) {
        // Implement efficiency calculation logic
        return metric.getEfficiency().doubleValue();
    }

    @Transactional(readOnly = true)
    public Double getAverageProcessedValue(Long resourceId, String dataType, LocalDateTime startTime) {
        return processedDataRepository.calculateAverageValue(resourceId, dataType, startTime);
    }
}