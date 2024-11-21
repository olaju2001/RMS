package com.kisters.repository;
import com.kisters.model.ProcessedData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProcessedDataRepository extends JpaRepository<ProcessedData, Long> {

    List<ProcessedData> findByResourceId(Long resourceId);

    @Query("SELECT p FROM ProcessedData p WHERE p.resource.id = :resourceId " +
            "AND p.timestamp BETWEEN :startTime AND :endTime")
    List<ProcessedData> findByResourceIdAndTimeRange(
            @Param("resourceId") Long resourceId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    @Query("SELECT AVG(p.processedValue) FROM ProcessedData p " +
            "WHERE p.resource.id = :resourceId AND p.dataType = :dataType " +
            "AND p.timestamp >= :startTime")
    Double calculateAverageValue(
            @Param("resourceId") Long resourceId,
            @Param("dataType") String dataType,
            @Param("startTime") LocalDateTime startTime
    );
}