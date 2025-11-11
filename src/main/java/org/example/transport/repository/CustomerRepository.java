package org.example.transport.repository;

import org.example.transport.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Customer entity
 * V2.0 - New repository with pagination and search capabilities
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * Find all active customers
     */
    List<Customer> findByActiveTrue();

    /**
     * Find customers by name (case-insensitive partial match)
     */
    @Query("SELECT c FROM Customer c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Customer> findByNameContaining(@Param("name") String name, Pageable pageable);

    /**
     * Find customers by address (case-insensitive partial match)
     */
    @Query("SELECT c FROM Customer c WHERE LOWER(c.address) LIKE LOWER(CONCAT('%', :address, '%'))")
    List<Customer> findByAddressContaining(@Param("address") String address);

    /**
     * Find customers within a geographic area
     */
    @Query("SELECT c FROM Customer c WHERE " +
           "c.latitude BETWEEN :minLat AND :maxLat AND " +
           "c.longitude BETWEEN :minLon AND :maxLon")
    List<Customer> findByLocationArea(
            @Param("minLat") Double minLat,
            @Param("maxLat") Double maxLat,
            @Param("minLon") Double minLon,
            @Param("maxLon") Double maxLon
    );

    /**
     * Search customers with pagination
     */
    @Query("SELECT c FROM Customer c WHERE " +
           "LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.address) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Customer> searchCustomers(@Param("searchTerm") String searchTerm, Pageable pageable);
}
