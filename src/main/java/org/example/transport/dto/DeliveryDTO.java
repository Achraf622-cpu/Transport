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
    
    @NotBlank(message = "Delivery address is required")
    private String address;
    
    @NotNull(message = "Latitude is required")
    private Double latitude;
    
    @NotNull(message = "Longitude is required")
    private Double longitude;
    
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
