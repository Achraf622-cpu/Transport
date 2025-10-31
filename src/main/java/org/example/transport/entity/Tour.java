package org.example.transport.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.transport.enums.OptimizationAlgorithm;

import java.time.LocalDate;
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

    @Column
    private Double totalDistanceKm;

    @Column
    private Double totalWeightKg;

    @Column
    private Double totalVolumeM3;

    @Column
    private Boolean optimized = false;

    public void addDelivery(Delivery delivery) {
        deliveries.add(delivery);
        delivery.setTour(this);
    }

    public void removeDelivery(Delivery delivery) {
        deliveries.remove(delivery);
        delivery.setTour(null);
    }
}
