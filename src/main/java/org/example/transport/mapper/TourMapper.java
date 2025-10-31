package org.example.transport.mapper;

import org.example.transport.dto.TourDTO;
import org.example.transport.entity.Delivery;
import org.example.transport.entity.Tour;

import java.util.stream.Collectors;

/**
 * Mapper for Tour entity and DTO
 */
public class TourMapper {

    public static TourDTO toDTO(Tour tour) {
        if (tour == null) {
            return null;
        }
        return new TourDTO(
                tour.getId(),
                tour.getTourDate(),
                tour.getVehicle() != null ? tour.getVehicle().getId() : null,
                tour.getWarehouse() != null ? tour.getWarehouse().getId() : null,
                tour.getDeliveries() != null ? 
                    tour.getDeliveries().stream()
                        .map(Delivery::getId)
                        .collect(Collectors.toList()) : null,
                tour.getAlgorithm(),
                tour.getTotalDistanceKm(),
                tour.getTotalWeightKg(),
                tour.getTotalVolumeM3(),
                tour.getOptimized()
        );
    }
}
