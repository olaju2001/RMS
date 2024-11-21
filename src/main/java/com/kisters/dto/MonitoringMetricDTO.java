package com.kisters.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonitoringMetricDTO {
    private Long id;
    
    @NotNull(message = "Timestamp is required")
    private LocalDateTime timestamp;
    
    @NotNull(message = "Resource ID is required")
    private Long resourceId;
    
    @NotNull(message = "Energy output is required")
    @PositiveOrZero(message = "Energy output must not be negative")
    private BigDecimal energyOutput;
    
    @NotNull(message = "Efficiency is required")
    @PositiveOrZero(message = "Efficiency must not be negative")
    private BigDecimal efficiency;
    
    private String weatherConditions;
}
