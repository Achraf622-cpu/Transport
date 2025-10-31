package org.example.transport.controller;

import org.example.transport.dto.VehicleDTO;
import org.example.transport.enums.VehicleType;
import org.example.transport.service.VehicleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Vehicle operations
 */
@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public ResponseEntity<List<VehicleDTO>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleDTO> getVehicleById(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.getVehicleById(id));
    }

    @PostMapping
    public ResponseEntity<VehicleDTO> createVehicle(@RequestBody VehicleDTO vehicleDTO) {
        VehicleDTO created = vehicleService.createVehicle(vehicleDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleDTO> updateVehicle(
            @PathVariable Long id,
            @RequestBody VehicleDTO vehicleDTO) {
        return ResponseEntity.ok(vehicleService.updateVehicle(id, vehicleDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available")
    public ResponseEntity<List<VehicleDTO>> getAvailableVehicles() {
        return ResponseEntity.ok(vehicleService.getAvailableVehicles());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<VehicleDTO>> getVehiclesByType(@PathVariable VehicleType type) {
        return ResponseEntity.ok(vehicleService.getVehiclesByType(type));
    }

    @GetMapping("/capacity")
    public ResponseEntity<List<VehicleDTO>> findAvailableVehiclesWithCapacity(
            @RequestParam Double weight,
            @RequestParam Double volume) {
        return ResponseEntity.ok(vehicleService.findAvailableVehiclesWithCapacity(weight, volume));
    }
}
