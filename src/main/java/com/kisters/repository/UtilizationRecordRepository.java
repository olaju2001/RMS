package com.kisters.repository;

import com.kisters.model.UtilizationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UtilizationRecordRepository extends JpaRepository<UtilizationRecord, Long> {
    List<UtilizationRecord> findByResourceId(Long resourceId);
    
    @Query("SELECT u FROM UtilizationRecord u WHERE " +
           "u.resource.id = :resourceId AND " +
           "u.timestamp BETWEEN :startTime AND :endTime " +
           "ORDER BY u.timestamp DESC")
    List<UtilizationRecord> findUtilizationHistory(
        @Param("resourceId") Long resourceId,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );

    @Query("SELECT AVG(u.efficiency) FROM UtilizationRecord u " +
           "WHERE u.resource.id = :resourceId AND " +
           "u.timestamp >= :startDate " +
           "GROUP BY FUNCTION('date_trunc', 'day', u.timestamp) " +
           "ORDER BY FUNCTION('date_trunc', 'day', u.timestamp)")
    List<Double> getDailyAverageEfficiency(
        @Param("resourceId") Long resourceId,
        @Param("startDate") LocalDateTime startDate
    );

    @Query("SELECT u FROM UtilizationRecord u WHERE " +
           "u.resource.id = :resourceId AND " +
           "u.efficiency < :threshold AND " +
           "u.timestamp >= :startTime")
    List<UtilizationRecord> findLowEfficiencyRecords(
        @Param("resourceId") Long resourceId,
        @Param("threshold") Double threshold,
        @Param("startTime") LocalDateTime startTime
    );
}
