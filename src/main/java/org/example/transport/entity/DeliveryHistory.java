package org.example.transport.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Entity representing delivery history
 * V2.0 - Automatically created when a Tour is marked as COMPLETED
 * Used for AI-based pattern analysis and optimization
 */
@Entity
@Table(name = "delivery_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Delivery date is required")
    @Column(nullable = false)
    private LocalDate deliveryDate;

    @NotNull(message = "Day of week is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    // Customer information at the time of delivery
    @NotNull(message = "Customer is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false, length = 500)
    private String deliveryAddress;

    @NotNull
    @Column(nullable = false)
    private Double latitude;

    @NotNull
    @Column(nullable = false)
    private Double longitude;

    // Tour information (reference to tour, nullable as tour might be deleted)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id")
    private Tour tour;

    @Column
    private Integer sequenceInTour;

    // Timing information
    @Column
    private LocalTime plannedTime;

    @Column
    private LocalTime actualTime;

    @Column(name = "delay_minutes")
    private Integer delayMinutes; // actualTime - plannedTime in minutes

    @Column
    private String preferredTimeSlot;

    // Delivery details
    @Column(name = "weight_kg")
    private Double weightKg;

    @Column(name = "volume_m3")
    private Double volumeM3;

    @Column
    private String deliveryStatus;

    // Vehicle used
    @Column
    private String vehicleType;

    @Column
    private String vehicleRegistration;

    // Additional metrics
    @Column
    private Double distanceFromWarehouseKm;

    @Column
    private Double distanceFromPreviousDeliveryKm;

    @Column
    private String notes;
}
