package org.example.transport.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.transport.enums.DeliveryStatus;

/**
 * Entity representing a delivery
 */
@Entity
@Table(name = "deliveries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"tour"})
@EqualsAndHashCode(exclude = {"tour"})
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Delivery address is required")
    @Column(nullable = false)
    private String address;

    @NotNull(message = "Latitude is required")
    @Column(nullable = false)
    private Double latitude;

    @NotNull(message = "Longitude is required")
    @Column(nullable = false)
    private Double longitude;

    @NotNull(message = "Weight is required")
    @Positive(message = "Weight must be positive")
    @Column(nullable = false)
    private Double weightKg;

    @NotNull(message = "Volume is required")
    @Positive(message = "Volume must be positive")
    @Column(nullable = false)
    private Double volumeM3;

    @Column
    private String preferredTimeSlot; // Format: "09:00-11:00"

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus status = DeliveryStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id")
    private Tour tour;

    @Column
    private Integer sequenceInTour; // Order in the tour
}
