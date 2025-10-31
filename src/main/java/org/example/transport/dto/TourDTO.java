package org.example.transport.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.transport.enums.OptimizationAlgorithm;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TourDTO {
    private Long id;
    
    @NotNull(message = "Tour date is required")
    private LocalDate tourDate;
    
    @NotNull(message = "Vehicle ID is required")
    private Long vehicleId;
    
    @NotNull(message = "Warehouse ID is required")
    private Long warehouseId;
    
    private List<Long> deliveryIds;
    private OptimizationAlgorithm algorithm;
    private Double totalDistanceKm;
    private Double totalWeightKg;
    private Double totalVolumeM3;
    private Boolean optimized;
}
