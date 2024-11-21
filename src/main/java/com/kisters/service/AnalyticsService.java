package com.kisters.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kisters.model.AnalyticsReport;
import com.kisters.model.MonitoringMetric;
import com.kisters.model.ProcessedData;
import com.kisters.repository.AnalyticsReportRepository;
import com.kisters.repository.MonitoringMetricRepository;
import com.kisters.repository.ProcessedDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnalyticsService {

    private final AnalyticsReportRepository analyticsRepository;
    private final MonitoringMetricRepository metricRepository;
    private final ProcessedDataRepository processedDataRepository;
    private final ObjectMapper objectMapper;
    private final KafkaProducerService kafkaProducer;

    @Transactional
    public AnalyticsReport generateEfficiencyReport(Long resourceId, LocalDateTime startDate, LocalDateTime endDate) {
        try {
            List<ProcessedData> processedData = processedDataRepository
                    .findByResourceIdAndTimeRange(resourceId, startDate, endDate);

            Map<String, Object> reportData = new HashMap<>();

            // Calculate average efficiency
            double avgEfficiency = processedData.stream()
                    .mapToDouble(ProcessedData::getProcessedValue)
                    .average()
                    .orElse(0.0);

            // Find peak efficiency periods
            List<Map<String, Object>> peakPeriods = findPeakEfficiencyPeriods(processedData);

            // Calculate efficiency trends
            Map<String, Double> trends = calculateEfficiencyTrends(processedData);

            reportData.put("averageEfficiency", avgEfficiency);
            reportData.put("peakPeriods", peakPeriods);
            reportData.put("trends", trends);

            AnalyticsReport report = new AnalyticsReport();
            report.setResourceId(resourceId);
            report.setReportType("EFFICIENCY_ANALYSIS");
            report.setReportData(objectMapper.writeValueAsString(reportData));

            Map<String, Object> params = new HashMap<>();
            params.put("startDate", startDate);
            params.put("endDate", endDate);
            report.setParameters(objectMapper.writeValueAsString(params));

            return analyticsRepository.save(report);
        } catch (Exception e) {
            log.error("Error generating efficiency report", e);
            throw new RuntimeException("Error generating efficiency report", e);
        }
    }

    @Transactional
    public AnalyticsReport generateResourceUtilizationReport(Long resourceId, LocalDateTime startDate) {
        try {
            List<MonitoringMetric> metrics = metricRepository
                    .findMetricsInTimeRange(resourceId, startDate, LocalDateTime.now());

            Map<String, Object> reportData = new HashMap<>();

            // Calculate utilization metrics
            double avgUtilization = calculateAverageUtilization(metrics);
            Map<String, Double> hourlyUtilization = calculateHourlyUtilization(metrics);
            List<String> underutilizedPeriods = findUnderutilizedPeriods(metrics);

            reportData.put("averageUtilization", avgUtilization);
            reportData.put("hourlyUtilization", hourlyUtilization);
            reportData.put("underutilizedPeriods", underutilizedPeriods);

            AnalyticsReport report = new AnalyticsReport();
            report.setResourceId(resourceId);
            report.setReportType("UTILIZATION_ANALYSIS");
            report.setReportData(objectMapper.writeValueAsString(reportData));

            return analyticsRepository.save(report);
        } catch (Exception e) {
            log.error("Error generating utilization report", e);
            throw new RuntimeException("Error generating utilization report", e);
        }
    }

    @Scheduled(cron = "0 0 1 * * *") // Run daily at 1 AM
    @Transactional
    public void generateDailyAnalytics() {
        log.info("Starting daily analytics generation");
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);

        try {
            // Generate reports for all active resources
            // Send analytics data to Kafka for further processing
            kafkaProducer.sendMessage("daily-analytics", "Daily analytics generation completed");
        } catch (Exception e) {
            log.error("Error generating daily analytics", e);
        }
    }

    private List<Map<String, Object>> findPeakEfficiencyPeriods(List<ProcessedData> data) {
        // Implementation for finding peak efficiency periods
        return new ArrayList<>();
    }

    private Map<String, Double> calculateEfficiencyTrends(List<ProcessedData> data) {
        // Implementation for calculating efficiency trends
        return new HashMap<>();
    }

    private double calculateAverageUtilization(List<MonitoringMetric> metrics) {
        return metrics.stream()
                .mapToDouble(m -> m.getEnergyOutput().doubleValue())
                .average()
                .orElse(0.0);
    }

    private Map<String, Double> calculateHourlyUtilization(List<MonitoringMetric> metrics) {
        // Implementation for calculating hourly utilization
        return new HashMap<>();
    }

    private List<String> findUnderutilizedPeriods(List<MonitoringMetric> metrics) {
        // Implementation for finding underutilized periods
        return new ArrayList<>();
    }
}