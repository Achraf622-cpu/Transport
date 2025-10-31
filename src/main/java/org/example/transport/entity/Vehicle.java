package org.example.transport.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.transport.enums.VehicleType;

/**
 * Entity representing a vehicle
 */
@Entity
@Table(name = "vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Registration number is required")
    @Column(nullable = false, unique = true)
    private String registrationNumber;

    @NotNull(message = "Vehicle type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleType type;

    @NotNull(message = "Max weight is required")
    @Column(nullable = false)
    private Double maxWeightKg;

    @NotNull(message = "Max volume is required")
    @Column(nullable = false)
    private Double maxVolumeM3;

    @Column
    private Boolean available = true;

    public Vehicle(String registrationNumber, VehicleType type) {
        this.registrationNumber = registrationNumber;
        this.type = type;
        this.maxWeightKg = type.getMaxWeightKg();
        this.maxVolumeM3 = type.getMaxVolumeM3();
        this.available = true;
    }
}
