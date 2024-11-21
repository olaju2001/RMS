package com.kisters.controller;

import com.kisters.dto.MonitoringMetricDTO;
import com.kisters.service.MonitoringMetricService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/metrics")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Monitoring Metrics", description = "Operations for managing resource monitoring metrics")
public class MonitoringMetricController {
    private final MonitoringMetricService monitoringMetricService;

    @PostMapping
    @Operation(summary = "Record new metric", description = "Records a new monitoring metric for a resource")
    public ResponseEntity<MonitoringMetricDTO> createMetric(@Valid @RequestBody MonitoringMetricDTO metricDTO) {
        log.info("Recording new metric for resource id: {}", metricDTO.getResourceId());
        MonitoringMetricDTO created = monitoringMetricService.createMetric(metricDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/resource/{resourceId}")
    @Operation(summary = "Get metrics by resource", description = "Retrieves metrics for a specific resource within a time range")
    public ResponseEntity<List<MonitoringMetricDTO>> getMetricsForResource(
            @PathVariable Long resourceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        log.info("Fetching metrics for resource id: {} between {} and {}", resourceId, startTime, endTime);
        List<MonitoringMetricDTO> metrics = monitoringMetricService.getMetricsForResource(resourceId, startTime, endTime);
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/resource/{resourceId}/efficiency")
    @Operation(summary = "Calculate resource efficiency", description = "Calculates average efficiency for a resource within a time range")
    public ResponseEntity<Double> calculateResourceEfficiency(
            @PathVariable Long resourceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        log.info("Calculating efficiency for resource id: {} between {} and {}", resourceId, startTime, endTime);
        Double efficiency = monitoringMetricService.calculateResourceEfficiency(resourceId, startTime, endTime);
        return ResponseEntity.ok(efficiency);
    }

    @GetMapping("/resource/{resourceId}/hourly-output")
    @Operation(summary = "Get hourly output", description = "Retrieves hourly average output for a resource")
    public ResponseEntity<List<Double>> getHourlyAverageOutput(
            @PathVariable Long resourceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime) {
        log.info("Getting hourly average output for resource id: {} from {}", resourceId, startTime);
        List<Double> hourlyOutput = monitoringMetricService.getHourlyAverageOutput(resourceId, startTime);
        return ResponseEntity.ok(hourlyOutput);
    }
}
