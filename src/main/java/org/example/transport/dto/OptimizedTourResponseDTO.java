package org.example.transport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.transport.enums.OptimizationAlgorithm;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptimizedTourResponseDTO {
    private Long tourId;
    private OptimizationAlgorithm algorithm;
    private List<DeliveryDTO> orderedDeliveries;
    private Double totalDistanceKm;
    private Double totalWeightKg;
    private Double totalVolumeM3;
}
