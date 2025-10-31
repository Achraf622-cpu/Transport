package org.example.transport.service;

import org.example.transport.dto.VehicleDTO;
import org.example.transport.entity.Vehicle;
import org.example.transport.enums.VehicleType;
import org.example.transport.exception.ResourceNotFoundException;
import org.example.transport.repository.VehicleRepository;
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
 * Unit tests for VehicleService
 */
@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    private VehicleService vehicleService;

    @BeforeEach
    void setUp() {
        vehicleService = new VehicleService();
        vehicleService.setVehicleRepository(vehicleRepository);
    }

    @Test
    void testGetAllVehicles() {
        Vehicle vehicle1 = new Vehicle("ABC123", VehicleType.VAN);
        vehicle1.setId(1L);
        Vehicle vehicle2 = new Vehicle("XYZ789", VehicleType.TRUCK);
        vehicle2.setId(2L);

        when(vehicleRepository.findAll()).thenReturn(Arrays.asList(vehicle1, vehicle2));

        List<VehicleDTO> vehicles = vehicleService.getAllVehicles();

        assertEquals(2, vehicles.size());
        verify(vehicleRepository, times(1)).findAll();
    }

    @Test
    void testGetVehicleById_Success() {
        Vehicle vehicle = new Vehicle("ABC123", VehicleType.VAN);
        vehicle.setId(1L);

        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));

        VehicleDTO result = vehicleService.getVehicleById(1L);

        assertNotNull(result);
        assertEquals("ABC123", result.getRegistrationNumber());
        assertEquals(VehicleType.VAN, result.getType());
    }

    @Test
    void testGetVehicleById_NotFound() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            vehicleService.getVehicleById(1L);
        });
    }

    @Test
    void testCreateVehicle() {
        VehicleDTO vehicleDTO = new VehicleDTO(null, "ABC123", VehicleType.VAN, 800.0, 10.0, true);
        Vehicle vehicle = new Vehicle("ABC123", VehicleType.VAN);
        vehicle.setId(1L);

        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);

        VehicleDTO result = vehicleService.createVehicle(vehicleDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
    }

    @Test
    void testGetAvailableVehicles() {
        Vehicle vehicle = new Vehicle("ABC123", VehicleType.VAN);
        vehicle.setId(1L);
        vehicle.setAvailable(true);

        when(vehicleRepository.findByAvailable(true)).thenReturn(Arrays.asList(vehicle));

        List<VehicleDTO> vehicles = vehicleService.getAvailableVehicles();

        assertEquals(1, vehicles.size());
        assertTrue(vehicles.get(0).getAvailable());
    }
}
