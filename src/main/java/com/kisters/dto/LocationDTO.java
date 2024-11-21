package com.kisters.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDTO {
    @Pattern(regexp = "^-?\\d+\\.\\d+$", message = "Invalid latitude format")
    private String latitude;
    
    @Pattern(regexp = "^-?\\d+\\.\\d+$", message = "Invalid longitude format")
    private String longitude;
    
    private String address;
}
