package org.example.transport.service;

import org.example.transport.dto.WarehouseDTO;
import org.example.transport.entity.Warehouse;
import org.example.transport.exception.ResourceNotFoundException;
import org.example.transport.mapper.WarehouseMapper;
import org.example.transport.repository.WarehouseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for managing warehouses
 */
public class WarehouseService {

    private static final Logger logger = LoggerFactory.getLogger(WarehouseService.class);

    private WarehouseRepository warehouseRepository;

    public void setWarehouseRepository(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    public List<WarehouseDTO> getAllWarehouses() {
        logger.info("Fetching all warehouses");
        return warehouseRepository.findAll().stream()
                .map(WarehouseMapper::toDTO)
                .collect(Collectors.toList());
    }

    public WarehouseDTO getWarehouseById(Long id) {
        logger.info("Fetching warehouse with id: {}", id);
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", id));
        return WarehouseMapper.toDTO(warehouse);
    }

    public WarehouseDTO createWarehouse(WarehouseDTO warehouseDTO) {
        logger.info("Creating new warehouse: {}", warehouseDTO.getName());
        Warehouse warehouse = WarehouseMapper.toEntity(warehouseDTO);
        Warehouse saved = warehouseRepository.save(warehouse);
        logger.info("Warehouse created with id: {}", saved.getId());
        return WarehouseMapper.toDTO(saved);
    }

    public WarehouseDTO updateWarehouse(Long id, WarehouseDTO warehouseDTO) {
        logger.info("Updating warehouse with id: {}", id);
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", id));
        WarehouseMapper.updateEntityFromDTO(warehouseDTO, warehouse);
        Warehouse updated = warehouseRepository.save(warehouse);
        logger.info("Warehouse updated: {}", id);
        return WarehouseMapper.toDTO(updated);
    }

    public void deleteWarehouse(Long id) {
        logger.info("Deleting warehouse with id: {}", id);
        if (!warehouseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Warehouse", id);
        }
        warehouseRepository.deleteById(id);
        logger.info("Warehouse deleted: {}", id);
    }

    public Optional<WarehouseDTO> findByName(String name) {
        logger.info("Finding warehouse by name: {}", name);
        return warehouseRepository.findByName(name)
                .map(WarehouseMapper::toDTO);
    }
}
