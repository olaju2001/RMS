package com.kisters.controller;

import com.kisters.dto.UtilizationRecordDTO;
import com.kisters.service.UtilizationRecordService;
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
@RequestMapping("/api/v1/utilization")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Utilization Records", description = "Operations for managing resource utilization records")
public class UtilizationRecordController {
    private final UtilizationRecordService utilizationRecordService;

    @PostMapping
    @Operation(summary = "Create utilization record", description = "Creates a new utilization record for a resource")
    public ResponseEntity<UtilizationRecordDTO> createRecord(@Valid @RequestBody UtilizationRecordDTO recordDTO) {
        log.info("Creating utilization record for resource id: {}", recordDTO.getResourceId());
        UtilizationRecordDTO created = utilizationRecordService.createRecord(recordDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/resource/{resourceId}/history")
    @Operation(summary = "Get utilization history", description = "Retrieves utilization history for a resource within a time range")
    public ResponseEntity<List<UtilizationRecordDTO>> getUtilizationHistory(
            @PathVariable Long resourceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        log.info("Fetching utilization history for resource id: {} between {} and {}", resourceId, startTime, endTime);
        List<UtilizationRecordDTO> history = utilizationRecordService.getUtilizationHistory(resourceId, startTime, endTime);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/resource/{resourceId}/efficiency-trend")
    @Operation(summary = "Get efficiency trend", description = "Retrieves daily efficiency trend for a resource")
    public ResponseEntity<List<Double>> getDailyEfficiencyTrend(
            @PathVariable Long resourceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate) {
        log.info("Getting daily efficiency trend for resource id: {} from {}", resourceId, startDate);
        List<Double> trend = utilizationRecordService.getDailyEfficiencyTrend(resourceId, startDate);
        return ResponseEntity.ok(trend);
    }

    @GetMapping("/resource/{resourceId}/low-efficiency")
    @Operation(summary = "Find low efficiency periods", description = "Identifies periods of low efficiency for a resource")
    public ResponseEntity<List<UtilizationRecordDTO>> findLowEfficiencyPeriods(
            @PathVariable Long resourceId,
            @RequestParam Double threshold,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime) {
        log.info("Finding low efficiency periods for resource id: {} below {}", resourceId, threshold);
        List<UtilizationRecordDTO> lowEfficiencyRecords = 
            utilizationRecordService.findLowEfficiencyPeriods(resourceId, threshold, startTime);
        return ResponseEntity.ok(lowEfficiencyRecords);
    }
}
