package com.kisters.service;

import com.kisters.dto.MonitoringMetricDTO;
import com.kisters.exception.ResourceNotFoundException;
import com.kisters.model.MonitoringMetric;
import com.kisters.model.Resource;
import com.kisters.repository.MonitoringMetricRepository;
import com.kisters.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MonitoringMetricService {
    private final MonitoringMetricRepository monitoringMetricRepository;
    private final ResourceRepository resourceRepository;
    private final ModelMapper modelMapper;

    public MonitoringMetricDTO createMetric(MonitoringMetricDTO metricDTO) {
        log.info("Creating new metric for resource id: {}", metricDTO.getResourceId());
        Resource resource = resourceRepository.findById(metricDTO.getResourceId())
            .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id: " + metricDTO.getResourceId()));

        MonitoringMetric metric = modelMapper.map(metricDTO, MonitoringMetric.class);
        metric.setResource(resource);
        MonitoringMetric savedMetric = monitoringMetricRepository.save(metric);
        return modelMapper.map(savedMetric, MonitoringMetricDTO.class);
    }

    public List<MonitoringMetricDTO> getMetricsForResource(Long resourceId, LocalDateTime startTime, LocalDateTime endTime) {
        log.info("Fetching metrics for resource id: {} between {} and {}", resourceId, startTime, endTime);
        return monitoringMetricRepository.findMetricsInTimeRange(resourceId, startTime, endTime).stream()
            .map(metric -> modelMapper.map(metric, MonitoringMetricDTO.class))
            .collect(Collectors.toList());
    }

    public Double calculateResourceEfficiency(Long resourceId, LocalDateTime startTime, LocalDateTime endTime) {
        log.info("Calculating efficiency for resource id: {} between {} and {}", resourceId, startTime, endTime);
        return monitoringMetricRepository.calculateAverageEfficiency(resourceId, startTime, endTime);
    }

    public List<Double> getHourlyAverageOutput(Long resourceId, LocalDateTime startTime) {
        log.info("Getting hourly average output for resource id: {} from {}", resourceId, startTime);
        return monitoringMetricRepository.getHourlyAverageOutput(resourceId, startTime);
    }
}
