package org.example.transport.repository;

import org.example.transport.entity.Delivery;
import org.example.transport.enums.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository for Delivery entity
 */
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    List<Delivery> findByStatus(DeliveryStatus status);

    List<Delivery> findByTourId(Long tourId);

    @Query("SELECT d FROM Delivery d WHERE d.tour IS NULL AND d.status = :status")
    List<Delivery> findUnassignedDeliveriesByStatus(@Param("status") DeliveryStatus status);

    @Query("SELECT d FROM Delivery d WHERE d.tour IS NULL")
    List<Delivery> findUnassignedDeliveries();

    @Query("SELECT d FROM Delivery d WHERE d.tour.id = :tourId ORDER BY d.sequenceInTour ASC")
    List<Delivery> findByTourIdOrderBySequence(@Param("tourId") Long tourId);

    @Query("SELECT d FROM Delivery d WHERE d.status = :status AND d.weightKg <= :maxWeight AND d.volumeM3 <= :maxVolume")
    List<Delivery> findDeliveriesByStatusAndCapacity(
            @Param("status") DeliveryStatus status,
            @Param("maxWeight") Double maxWeight,
            @Param("maxVolume") Double maxVolume
    );
}
