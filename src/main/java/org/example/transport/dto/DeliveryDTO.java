package org.example.transport.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.transport.enums.DeliveryStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDTO {
    private Long id;
    
    // V2.0 - Customer reference
    @NotNull(message = "Customer ID is required")
    private Long customerId;
    
    private String customerName; // For display purposes
    
    // Optional specific address override
    private String specificAddress;
    private Double specificLatitude;
    private Double specificLongitude;
    
    // Effective address (computed from customer or specific)
    private String effectiveAddress;
    private Double effectiveLatitude;
    private Double effectiveLongitude;
    
    @NotNull(message = "Weight is required")
    @Positive(message = "Weight must be positive")
    private Double weightKg;
    
    @NotNull(message = "Volume is required")
    @Positive(message = "Volume must be positive")
    private Double volumeM3;
    
    private String preferredTimeSlot;
    private DeliveryStatus status;
    private Long tourId;
    private Integer sequenceInTour;
}
