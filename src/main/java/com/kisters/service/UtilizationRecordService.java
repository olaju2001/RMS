package com.kisters.service;

import com.kisters.dto.UtilizationRecordDTO;
import com.kisters.exception.ResourceNotFoundException;
import com.kisters.model.Resource;
import com.kisters.model.UtilizationRecord;
import com.kisters.repository.ResourceRepository;
import com.kisters.repository.UtilizationRecordRepository;
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
public class UtilizationRecordService {
    private final UtilizationRecordRepository utilizationRecordRepository;
    private final ResourceRepository resourceRepository;
    private final ModelMapper modelMapper;

    public UtilizationRecordDTO createRecord(UtilizationRecordDTO recordDTO) {
        log.info("Creating new utilization record for resource id: {}", recordDTO.getResourceId());
        Resource resource = resourceRepository.findById(recordDTO.getResourceId())
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id: " + recordDTO.getResourceId()));

        UtilizationRecord record = modelMapper.map(recordDTO, UtilizationRecord.class);
        record.setResource(resource);
        UtilizationRecord savedRecord = utilizationRecordRepository.save(record);
        return modelMapper.map(savedRecord, UtilizationRecordDTO.class);
    }

    public List<UtilizationRecordDTO> getUtilizationHistory(Long resourceId, LocalDateTime startTime, LocalDateTime endTime) {
        log.info("Fetching utilization history for resource id: {} between {} and {}", resourceId, startTime, endTime);
        return utilizationRecordRepository.findUtilizationHistory(resourceId, startTime, endTime).stream()
                .map(record -> modelMapper.map(record, UtilizationRecordDTO.class))
                .collect(Collectors.toList());
    }

    public List<Double> getDailyEfficiencyTrend(Long resourceId, LocalDateTime startDate) {
        log.info("Getting daily efficiency trend for resource id: {} from {}", resourceId, startDate);
        return utilizationRecordRepository.getDailyAverageEfficiency(resourceId, startDate);
    }

    public List<UtilizationRecordDTO> findLowEfficiencyPeriods(Long resourceId, Double threshold, LocalDateTime startTime) {
        log.info("Finding low efficiency periods for resource id: {} below {}", resourceId, threshold);
        return utilizationRecordRepository.findLowEfficiencyRecords(resourceId, threshold, startTime).stream()
                .map(record -> modelMapper.map(record, UtilizationRecordDTO.class))
                .collect(Collectors.toList());
    }
}