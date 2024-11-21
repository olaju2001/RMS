package com.kisters.dto;

import com.kisters.model.ResourceStatus;
import com.kisters.model.ResourceType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceDTO {
    private Long id;
    
    @NotNull(message = "Resource type is required")
    private ResourceType type;
    
    @NotNull(message = "Capacity is required")
    @Positive(message = "Capacity must be positive")
    private BigDecimal capacity;
    
    @NotNull(message = "Status is required")
    private ResourceStatus status;
    
    @Valid
    private LocationDTO location;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
