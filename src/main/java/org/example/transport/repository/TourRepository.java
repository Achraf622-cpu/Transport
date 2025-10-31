package org.example.transport.repository;

import org.example.transport.entity.Tour;
import org.example.transport.enums.OptimizationAlgorithm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for Tour entity
 */
public interface TourRepository extends JpaRepository<Tour, Long> {

    List<Tour> findByTourDate(LocalDate tourDate);

    List<Tour> findByVehicleId(Long vehicleId);

    List<Tour> findByWarehouseId(Long warehouseId);

    List<Tour> findByTourDateAndVehicleId(LocalDate tourDate, Long vehicleId);

    List<Tour> findByAlgorithm(OptimizationAlgorithm algorithm);

    @Query("SELECT t FROM Tour t WHERE t.tourDate = :date AND t.optimized = :optimized")
    List<Tour> findByTourDateAndOptimized(@Param("date") LocalDate date, @Param("optimized") Boolean optimized);

    @Query("SELECT t FROM Tour t WHERE t.tourDate BETWEEN :startDate AND :endDate")
    List<Tour> findByTourDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT AVG(t.totalDistanceKm) FROM Tour t WHERE t.algorithm = :algorithm AND t.optimized = true")
    Double getAverageDistanceByAlgorithm(@Param("algorithm") OptimizationAlgorithm algorithm);
}
