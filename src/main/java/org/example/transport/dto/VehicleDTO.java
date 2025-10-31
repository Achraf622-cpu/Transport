package org.example.transport.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.transport.enums.VehicleType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDTO {
    private Long id;
    
    @NotBlank(message = "Registration number is required")
    private String registrationNumber;
    
    @NotNull(message = "Vehicle type is required")
    private VehicleType type;
    
    private Double maxWeightKg;
    private Double maxVolumeM3;
    private Boolean available;
}
