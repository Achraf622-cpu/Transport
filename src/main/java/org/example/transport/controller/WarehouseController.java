package org.example.transport.controller;

import org.example.transport.dto.WarehouseDTO;
import org.example.transport.service.WarehouseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Warehouse operations
 */
@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping
    public ResponseEntity<List<WarehouseDTO>> getAllWarehouses() {
        return ResponseEntity.ok(warehouseService.getAllWarehouses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseDTO> getWarehouseById(@PathVariable Long id) {
        return ResponseEntity.ok(warehouseService.getWarehouseById(id));
    }

    @PostMapping
    public ResponseEntity<WarehouseDTO> createWarehouse(@RequestBody WarehouseDTO warehouseDTO) {
        WarehouseDTO created = warehouseService.createWarehouse(warehouseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WarehouseDTO> updateWarehouse(
            @PathVariable Long id,
            @RequestBody WarehouseDTO warehouseDTO) {
        return ResponseEntity.ok(warehouseService.updateWarehouse(id, warehouseDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWarehouse(@PathVariable Long id) {
        warehouseService.deleteWarehouse(id);
        return ResponseEntity.noContent().build();
    }
}
