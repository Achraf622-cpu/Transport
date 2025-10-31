package org.example.transport.repository;

import org.example.transport.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repository for Warehouse entity
 */
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    Optional<Warehouse> findByName(String name);

    @Query("SELECT w FROM Warehouse w WHERE w.latitude = :lat AND w.longitude = :lon")
    Optional<Warehouse> findByCoordinates(@Param("lat") Double latitude, @Param("lon") Double longitude);
}
