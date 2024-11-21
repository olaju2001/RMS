package com.kisters.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "analytics_reports")
public class AnalyticsReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime generatedAt;

    @Column(nullable = false)
    private String reportType;

    @Column(columnDefinition = "jsonb")
    private String reportData;

    @Column(columnDefinition = "jsonb")
    private String parameters;

    @Column(name = "resource_id")
    private Long resourceId;

    @PrePersist
    protected void onCreate() {
        generatedAt = LocalDateTime.now();
    }
}
