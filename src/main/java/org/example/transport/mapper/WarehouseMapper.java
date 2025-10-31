package org.example.transport.mapper;

import org.example.transport.dto.WarehouseDTO;
import org.example.transport.entity.Warehouse;

/**
 * Mapper for Warehouse entity and DTO
 */
public class WarehouseMapper {

    public static WarehouseDTO toDTO(Warehouse warehouse) {
        if (warehouse == null) {
            return null;
        }
        return new WarehouseDTO(
                warehouse.getId(),
                warehouse.getName(),
                warehouse.getAddress(),
                warehouse.getLatitude(),
                warehouse.getLongitude(),
                warehouse.getOpeningTime(),
                warehouse.getClosingTime()
        );
    }

    public static Warehouse toEntity(WarehouseDTO dto) {
        if (dto == null) {
            return null;
        }
        return new Warehouse(
                dto.getId(),
                dto.getName(),
                dto.getAddress(),
                dto.getLatitude(),
                dto.getLongitude(),
                dto.getOpeningTime(),
                dto.getClosingTime()
        );
    }

    public static void updateEntityFromDTO(WarehouseDTO dto, Warehouse warehouse) {
        if (dto == null || warehouse == null) {
            return;
        }
        warehouse.setName(dto.getName());
        warehouse.setAddress(dto.getAddress());
        warehouse.setLatitude(dto.getLatitude());
        warehouse.setLongitude(dto.getLongitude());
        warehouse.setOpeningTime(dto.getOpeningTime());
        warehouse.setClosingTime(dto.getClosingTime());
    }
}
