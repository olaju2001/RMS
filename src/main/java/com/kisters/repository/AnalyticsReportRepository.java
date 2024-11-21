package com.kisters.repository;

import com.kisters.model.AnalyticsReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AnalyticsReportRepository extends JpaRepository<AnalyticsReport, Long> {

    List<AnalyticsReport> findByResourceIdAndReportType(Long resourceId, String reportType);

    @Query("SELECT a FROM AnalyticsReport a WHERE a.resourceId = :resourceId " +
            "AND a.reportType = :reportType AND a.generatedAt >= :startTime " +
            "ORDER BY a.generatedAt DESC LIMIT 1")
    Optional<AnalyticsReport> findLatestReport(
            @Param("resourceId") Long resourceId,
            @Param("reportType") String reportType,
            @Param("startTime") LocalDateTime startTime
    );
}