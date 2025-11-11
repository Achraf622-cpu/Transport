package org.example.transport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryHistoryDTO {
    private Long id;
    private LocalDate deliveryDate;
    private DayOfWeek dayOfWeek;
    
    // Customer info
    private Long customerId;
    private String customerName;
    private String deliveryAddress;
    private Double latitude;
    private Double longitude;
    
    // Tour info
    private Long tourId;
    private Integer sequenceInTour;
    
    // Timing
    private LocalTime plannedTime;
    private LocalTime actualTime;
    private Integer delayMinutes;
    private String preferredTimeSlot;
    
    // Delivery details
    private Double weightKg;
    private Double volumeM3;
    private String deliveryStatus;
    
    // Vehicle
    private String vehicleType;
    private String vehicleRegistration;
    
    // Metrics
    private Double distanceFromWarehouseKm;
    private Double distanceFromPreviousDeliveryKm;
    private String notes;
}
