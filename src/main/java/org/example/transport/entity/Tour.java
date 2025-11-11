package org.example.transport.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.transport.enums.OptimizationAlgorithm;
import org.example.transport.enums.TourStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a tour (delivery route)
 */
@Entity
@Table(name = "tours")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"deliveries"})
@EqualsAndHashCode(exclude = {"deliveries"})
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Tour date is required")
    @Column(nullable = false)
    private LocalDate tourDate;

    @NotNull(message = "Vehicle is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @NotNull(message = "Warehouse is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sequenceInTour ASC")
    private List<Delivery> deliveries = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column
    private OptimizationAlgorithm algorithm;

    @Column(name = "total_distance_km")
    private Double totalDistanceKm;

    @Column(name = "total_weight_kg")
    private Double totalWeightKg;

    @Column(name = "total_volume_m3")
    private Double totalVolumeM3;

    @Column
    private Boolean optimized = false;

    // V2.0 - Tour status management
    @NotNull(message = "Tour status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TourStatus status = TourStatus.PENDING;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "completion_time")
    private LocalDateTime completionTime;

    public void addDelivery(Delivery delivery) {
        deliveries.add(delivery);
        delivery.setTour(this);
    }

    public void removeDelivery(Delivery delivery) {
        deliveries.remove(delivery);
        delivery.setTour(null);
    }
}
