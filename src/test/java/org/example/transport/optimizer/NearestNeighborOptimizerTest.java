package org.example.transport.optimizer;

import org.example.transport.entity.Delivery;
import org.example.transport.entity.Tour;
import org.example.transport.entity.Vehicle;
import org.example.transport.entity.Warehouse;
import org.example.transport.enums.DeliveryStatus;
import org.example.transport.enums.VehicleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for NearestNeighborOptimizer
 */
class NearestNeighborOptimizerTest {

    private NearestNeighborOptimizer optimizer;
    private Warehouse warehouse;
    private Vehicle vehicle;

    @BeforeEach
    void setUp() {
        optimizer = new NearestNeighborOptimizer();
        
        warehouse = new Warehouse();
        warehouse.setId(1L);
        warehouse.setName("Main Warehouse");
        warehouse.setAddress("Warehouse Address");
        warehouse.setLatitude(48.8566);
        warehouse.setLongitude(2.3522);
        warehouse.setOpeningTime(LocalTime.of(6, 0));
        warehouse.setClosingTime(LocalTime.of(22, 0));

        vehicle = new Vehicle("VAN123", VehicleType.VAN);
        vehicle.setId(1L);
    }

    @Test
    void testCalculateOptimalTour_EmptyDeliveries() {
        Tour tour = new Tour();
        tour.setId(1L);
        tour.setTourDate(LocalDate.now());
        tour.setVehicle(vehicle);
        tour.setWarehouse(warehouse);

        List<Delivery> result = optimizer.calculateOptimalTour(tour);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testCalculateOptimalTour_SingleDelivery() {
        Tour tour = new Tour();
        tour.setId(1L);
        tour.setTourDate(LocalDate.now());
        tour.setVehicle(vehicle);
        tour.setWarehouse(warehouse);

        Delivery delivery = new Delivery();
        delivery.setId(1L);
        delivery.setAddress("123 Main St");
        delivery.setLatitude(48.8600);
        delivery.setLongitude(2.3600);
        delivery.setWeightKg(10.0);
        delivery.setVolumeM3(0.5);
        delivery.setStatus(DeliveryStatus.PENDING);

        tour.addDelivery(delivery);

        List<Delivery> result = optimizer.calculateOptimalTour(tour);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(delivery.getId(), result.get(0).getId());
    }

    @Test
    void testCalculateOptimalTour_MultipleDeliveries() {
        Tour tour = new Tour();
        tour.setId(1L);
        tour.setTourDate(LocalDate.now());
        tour.setVehicle(vehicle);
        tour.setWarehouse(warehouse);

        // Delivery 1 - Close to warehouse
        Delivery delivery1 = new Delivery();
        delivery1.setId(1L);
        delivery1.setAddress("Close Location");
        delivery1.setLatitude(48.8570);
        delivery1.setLongitude(2.3530);
        delivery1.setWeightKg(5.0);
        delivery1.setVolumeM3(0.3);
        delivery1.setStatus(DeliveryStatus.PENDING);

        // Delivery 2 - Far from warehouse
        Delivery delivery2 = new Delivery();
        delivery2.setId(2L);
        delivery2.setAddress("Far Location");
        delivery2.setLatitude(48.9000);
        delivery2.setLongitude(2.4000);
        delivery2.setWeightKg(8.0);
        delivery2.setVolumeM3(0.6);
        delivery2.setStatus(DeliveryStatus.PENDING);

        // Delivery 3 - Medium distance
        Delivery delivery3 = new Delivery();
        delivery3.setId(3L);
        delivery3.setAddress("Medium Location");
        delivery3.setLatitude(48.8700);
        delivery3.setLongitude(2.3700);
        delivery3.setWeightKg(6.0);
        delivery3.setVolumeM3(0.4);
        delivery3.setStatus(DeliveryStatus.PENDING);

        tour.addDelivery(delivery1);
        tour.addDelivery(delivery2);
        tour.addDelivery(delivery3);

        List<Delivery> result = optimizer.calculateOptimalTour(tour);

        assertNotNull(result);
        assertEquals(3, result.size());
        
        // First delivery should be the closest to warehouse
        assertEquals(1L, result.get(0).getId());
    }
}
