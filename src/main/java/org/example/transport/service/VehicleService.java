package org.example.transport.service;

import org.example.transport.dto.VehicleDTO;
import org.example.transport.entity.Vehicle;
import org.example.transport.enums.VehicleType;
import org.example.transport.exception.ResourceNotFoundException;
import org.example.transport.mapper.VehicleMapper;
import org.example.transport.repository.VehicleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing vehicles
 */
public class VehicleService {

    private static final Logger logger = LoggerFactory.getLogger(VehicleService.class);

    private VehicleRepository vehicleRepository;

    public void setVehicleRepository(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public List<VehicleDTO> getAllVehicles() {
        logger.info("Fetching all vehicles");
        return vehicleRepository.findAll().stream()
                .map(VehicleMapper::toDTO)
                .collect(Collectors.toList());
    }

    public VehicleDTO getVehicleById(Long id) {
        logger.info("Fetching vehicle with id: {}", id);
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", id));
        return VehicleMapper.toDTO(vehicle);
    }

    public VehicleDTO createVehicle(VehicleDTO vehicleDTO) {
        logger.info("Creating new vehicle: {}", vehicleDTO.getRegistrationNumber());
        Vehicle vehicle = VehicleMapper.toEntity(vehicleDTO);
        Vehicle saved = vehicleRepository.save(vehicle);
        logger.info("Vehicle created with id: {}", saved.getId());
        return VehicleMapper.toDTO(saved);
    }

    public VehicleDTO updateVehicle(Long id, VehicleDTO vehicleDTO) {
        logger.info("Updating vehicle with id: {}", id);
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", id));
        VehicleMapper.updateEntityFromDTO(vehicleDTO, vehicle);
        Vehicle updated = vehicleRepository.save(vehicle);
        logger.info("Vehicle updated: {}", id);
        return VehicleMapper.toDTO(updated);
    }

    public void deleteVehicle(Long id) {
        logger.info("Deleting vehicle with id: {}", id);
        if (!vehicleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vehicle", id);
        }
        vehicleRepository.deleteById(id);
        logger.info("Vehicle deleted: {}", id);
    }

    public List<VehicleDTO> getAvailableVehicles() {
        logger.info("Fetching available vehicles");
        return vehicleRepository.findByAvailable(true).stream()
                .map(VehicleMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<VehicleDTO> getVehiclesByType(VehicleType type) {
        logger.info("Fetching vehicles by type: {}", type);
        return vehicleRepository.findByType(type).stream()
                .map(VehicleMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<VehicleDTO> findAvailableVehiclesWithCapacity(Double weight, Double volume) {
        logger.info("Finding available vehicles with capacity: weight={}, volume={}", weight, volume);
        return vehicleRepository.findAvailableVehiclesWithCapacity(weight, volume).stream()
                .map(VehicleMapper::toDTO)
                .collect(Collectors.toList());
    }
}
