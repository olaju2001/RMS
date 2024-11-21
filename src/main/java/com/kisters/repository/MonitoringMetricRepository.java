package com.kisters.repository;

import com.kisters.model.MonitoringMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MonitoringMetricRepository extends JpaRepository<MonitoringMetric, Long> {
    List<MonitoringMetric> findByResourceId(Long resourceId);
    
    @Query("SELECT m FROM MonitoringMetric m WHERE " +
           "m.resource.id = :resourceId AND " +
           "m.timestamp BETWEEN :startTime AND :endTime " +
           "ORDER BY m.timestamp DESC")
    List<MonitoringMetric> findMetricsInTimeRange(
        @Param("resourceId") Long resourceId,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );
    
    @Query("SELECT AVG(m.efficiency) FROM MonitoringMetric m " +
           "WHERE m.resource.id = :resourceId AND " +
           "m.timestamp BETWEEN :startTime AND :endTime")
    Double calculateAverageEfficiency(
        @Param("resourceId") Long resourceId,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );

    @Query("SELECT AVG(m.energyOutput) FROM MonitoringMetric m " +
           "WHERE m.resource.id = :resourceId AND " +
           "m.timestamp >= :startTime " +
           "GROUP BY FUNCTION('date_trunc', 'hour', m.timestamp) " +
           "ORDER BY FUNCTION('date_trunc', 'hour', m.timestamp)")
    List<Double> getHourlyAverageOutput(
        @Param("resourceId") Long resourceId,
        @Param("startTime") LocalDateTime startTime
    );
}
