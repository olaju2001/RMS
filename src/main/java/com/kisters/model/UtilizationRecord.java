package com.kisters.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "utilization_records")
@Data
@NoArgsConstructor
public class UtilizationRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id", nullable = false)
    private Resource resource;

    @Column(name = "usage_data", nullable = false, precision = 10, scale = 2)
    private BigDecimal usageData;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal efficiency;

    @Column
    private String notes;
}
