package org.example.transport.enums;

/**
 * Enum representing different types of vehicles with their capacity constraints
 * VAN: Max 800kg, 10m³
 * TRUCK: Max 3500kg, 40m³
 * BIKE: Max 15kg, 0.5m³
 */
public enum VehicleType {
    VAN(800.0, 10.0),
    TRUCK(3500.0, 40.0),
    BIKE(15.0, 0.5);

    private final Double maxWeightKg;
    private final Double maxVolumeM3;

    VehicleType(Double maxWeightKg, Double maxVolumeM3) {
        this.maxWeightKg = maxWeightKg;
        this.maxVolumeM3 = maxVolumeM3;
    }

    public Double getMaxWeightKg() {
        return maxWeightKg;
    }

    public Double getMaxVolumeM3() {
        return maxVolumeM3;
    }
}
