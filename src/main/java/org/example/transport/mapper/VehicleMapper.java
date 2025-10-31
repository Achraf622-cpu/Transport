package org.example.transport.mapper;

import org.example.transport.dto.VehicleDTO;
import org.example.transport.entity.Vehicle;

/**
 * Mapper for Vehicle entity and DTO
 */
public class VehicleMapper {

    public static VehicleDTO toDTO(Vehicle vehicle) {
        if (vehicle == null) {
            return null;
        }
        return new VehicleDTO(
                vehicle.getId(),
                vehicle.getRegistrationNumber(),
                vehicle.getType(),
                vehicle.getMaxWeightKg(),
                vehicle.getMaxVolumeM3(),
                vehicle.getAvailable()
        );
    }

    public static Vehicle toEntity(VehicleDTO dto) {
        if (dto == null) {
            return null;
        }
        Vehicle vehicle = new Vehicle();
        vehicle.setId(dto.getId());
        vehicle.setRegistrationNumber(dto.getRegistrationNumber());
        vehicle.setType(dto.getType());
        vehicle.setMaxWeightKg(dto.getMaxWeightKg() != null ? dto.getMaxWeightKg() : dto.getType().getMaxWeightKg());
        vehicle.setMaxVolumeM3(dto.getMaxVolumeM3() != null ? dto.getMaxVolumeM3() : dto.getType().getMaxVolumeM3());
        vehicle.setAvailable(dto.getAvailable() != null ? dto.getAvailable() : true);
        return vehicle;
    }

    public static void updateEntityFromDTO(VehicleDTO dto, Vehicle vehicle) {
        if (dto == null || vehicle == null) {
            return;
        }
        vehicle.setRegistrationNumber(dto.getRegistrationNumber());
        vehicle.setType(dto.getType());
        vehicle.setMaxWeightKg(dto.getMaxWeightKg() != null ? dto.getMaxWeightKg() : dto.getType().getMaxWeightKg());
        vehicle.setMaxVolumeM3(dto.getMaxVolumeM3() != null ? dto.getMaxVolumeM3() : dto.getType().getMaxVolumeM3());
        if (dto.getAvailable() != null) {
            vehicle.setAvailable(dto.getAvailable());
        }
    }
}
