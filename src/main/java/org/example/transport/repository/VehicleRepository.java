package org.example.transport.repository;

import org.example.transport.entity.Vehicle;
import org.example.transport.enums.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Vehicle entity
 */
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByRegistrationNumber(String registrationNumber);

    List<Vehicle> findByType(VehicleType type);

    List<Vehicle> findByAvailable(Boolean available);

    List<Vehicle> findByTypeAndAvailable(VehicleType type, Boolean available);

    @Query("SELECT v FROM Vehicle v WHERE v.available = true AND v.maxWeightKg >= :weight AND v.maxVolumeM3 >= :volume")
    List<Vehicle> findAvailableVehiclesWithCapacity(@Param("weight") Double weight, @Param("volume") Double volume);
}
