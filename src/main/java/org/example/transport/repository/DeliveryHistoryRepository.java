package org.example.transport.repository;

import org.example.transport.entity.DeliveryHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

/**
 * Repository for DeliveryHistory entity
 * V2.0 - Used for AI-based pattern analysis
 */
@Repository
public interface DeliveryHistoryRepository extends JpaRepository<DeliveryHistory, Long> {

    /**
     * Find delivery history by customer
     */
    List<DeliveryHistory> findByCustomerId(Long customerId);

    /**
     * Find delivery history by day of week
     */
    List<DeliveryHistory> findByDayOfWeek(DayOfWeek dayOfWeek);

    /**
     * Find delivery history by date range
     */
    @Query("SELECT dh FROM DeliveryHistory dh WHERE dh.deliveryDate BETWEEN :startDate AND :endDate")
    List<DeliveryHistory> findByDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    /**
     * Find deliveries with delays
     */
    @Query("SELECT dh FROM DeliveryHistory dh WHERE dh.delayMinutes > :minDelay ORDER BY dh.delayMinutes DESC")
    List<DeliveryHistory> findDelayedDeliveries(@Param("minDelay") Integer minDelay);

    /**
     * Get average delay by day of week
     */
    @Query("SELECT dh.dayOfWeek, AVG(dh.delayMinutes) FROM DeliveryHistory dh " +
           "WHERE dh.delayMinutes IS NOT NULL " +
           "GROUP BY dh.dayOfWeek")
    List<Object[]> getAverageDelayByDayOfWeek();

    /**
     * Get average delay by time slot
     */
    @Query("SELECT dh.preferredTimeSlot, AVG(dh.delayMinutes) FROM DeliveryHistory dh " +
           "WHERE dh.preferredTimeSlot IS NOT NULL AND dh.delayMinutes IS NOT NULL " +
           "GROUP BY dh.preferredTimeSlot")
    List<Object[]> getAverageDelayByTimeSlot();

    /**
     * Find recent delivery history with pagination
     */
    Page<DeliveryHistory> findAllByOrderByDeliveryDateDesc(Pageable pageable);

    /**
     * Find delivery history for a specific customer with pagination
     */
    Page<DeliveryHistory> findByCustomerIdOrderByDeliveryDateDesc(Long customerId, Pageable pageable);
}
