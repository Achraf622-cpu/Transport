package org.example.transport.service;

import org.example.transport.dto.DeliveryDTO;
import org.example.transport.entity.Delivery;
import org.example.transport.enums.DeliveryStatus;
import org.example.transport.exception.ResourceNotFoundException;
import org.example.transport.repository.DeliveryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for DeliveryService
 */
@ExtendWith(MockitoExtension.class)
class DeliveryServiceTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    private DeliveryService deliveryService;

    @BeforeEach
    void setUp() {
        deliveryService = new DeliveryService();
        deliveryService.setDeliveryRepository(deliveryRepository);
    }

    @Test
    void testGetAllDeliveries() {
        Delivery delivery1 = new Delivery();
        delivery1.setId(1L);
        delivery1.setAddress("123 Main St");
        delivery1.setStatus(DeliveryStatus.PENDING);

        Delivery delivery2 = new Delivery();
        delivery2.setId(2L);
        delivery2.setAddress("456 Oak Ave");
        delivery2.setStatus(DeliveryStatus.DELIVERED);

        when(deliveryRepository.findAll()).thenReturn(Arrays.asList(delivery1, delivery2));

        List<DeliveryDTO> deliveries = deliveryService.getAllDeliveries();

        assertEquals(2, deliveries.size());
        verify(deliveryRepository, times(1)).findAll();
    }

    @Test
    void testGetDeliveryById_Success() {
        Delivery delivery = new Delivery();
        delivery.setId(1L);
        delivery.setAddress("123 Main St");
        delivery.setLatitude(48.8566);
        delivery.setLongitude(2.3522);
        delivery.setWeightKg(10.0);
        delivery.setVolumeM3(0.5);
        delivery.setStatus(DeliveryStatus.PENDING);

        when(deliveryRepository.findById(1L)).thenReturn(Optional.of(delivery));

        DeliveryDTO result = deliveryService.getDeliveryById(1L);

        assertNotNull(result);
        assertEquals("123 Main St", result.getAddress());
        assertEquals(DeliveryStatus.PENDING, result.getStatus());
    }

    @Test
    void testGetDeliveryById_NotFound() {
        when(deliveryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            deliveryService.getDeliveryById(1L);
        });
    }

    @Test
    void testUpdateDeliveryStatus() {
        Delivery delivery = new Delivery();
        delivery.setId(1L);
        delivery.setAddress("123 Main St");
        delivery.setLatitude(48.8566);
        delivery.setLongitude(2.3522);
        delivery.setWeightKg(10.0);
        delivery.setVolumeM3(0.5);
        delivery.setStatus(DeliveryStatus.PENDING);

        when(deliveryRepository.findById(1L)).thenReturn(Optional.of(delivery));
        when(deliveryRepository.save(any(Delivery.class))).thenReturn(delivery);

        DeliveryDTO result = deliveryService.updateDeliveryStatus(1L, DeliveryStatus.IN_TRANSIT);

        assertNotNull(result);
        assertEquals(DeliveryStatus.IN_TRANSIT, result.getStatus());
        verify(deliveryRepository, times(1)).save(any(Delivery.class));
    }

    @Test
    void testGetDeliveriesByStatus() {
        Delivery delivery = new Delivery();
        delivery.setId(1L);
        delivery.setStatus(DeliveryStatus.PENDING);

        when(deliveryRepository.findByStatus(DeliveryStatus.PENDING))
                .thenReturn(Arrays.asList(delivery));

        List<DeliveryDTO> deliveries = deliveryService.getDeliveriesByStatus(DeliveryStatus.PENDING);

        assertEquals(1, deliveries.size());
        assertEquals(DeliveryStatus.PENDING, deliveries.get(0).getStatus());
    }
}
