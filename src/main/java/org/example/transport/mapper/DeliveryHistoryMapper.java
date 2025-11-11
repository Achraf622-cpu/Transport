package org.example.transport.mapper;

import org.example.transport.dto.DeliveryHistoryDTO;
import org.example.transport.entity.DeliveryHistory;

/**
 * Mapper for DeliveryHistory entity and DTO
 * V2.0
 */
public class DeliveryHistoryMapper {

    public static DeliveryHistoryDTO toDTO(DeliveryHistory history) {
        if (history == null) {
            return null;
        }
        return DeliveryHistoryDTO.builder()
                .id(history.getId())
                .deliveryDate(history.getDeliveryDate())
                .dayOfWeek(history.getDayOfWeek())
                .customerId(history.getCustomer() != null ? history.getCustomer().getId() : null)
                .customerName(history.getCustomerName())
                .deliveryAddress(history.getDeliveryAddress())
                .latitude(history.getLatitude())
                .longitude(history.getLongitude())
                .tourId(history.getTour() != null ? history.getTour().getId() : null)
                .sequenceInTour(history.getSequenceInTour())
                .plannedTime(history.getPlannedTime())
                .actualTime(history.getActualTime())
                .delayMinutes(history.getDelayMinutes())
                .preferredTimeSlot(history.getPreferredTimeSlot())
                .weightKg(history.getWeightKg())
                .volumeM3(history.getVolumeM3())
                .deliveryStatus(history.getDeliveryStatus())
                .vehicleType(history.getVehicleType())
                .vehicleRegistration(history.getVehicleRegistration())
                .distanceFromWarehouseKm(history.getDistanceFromWarehouseKm())
                .distanceFromPreviousDeliveryKm(history.getDistanceFromPreviousDeliveryKm())
                .notes(history.getNotes())
                .build();
    }
}
