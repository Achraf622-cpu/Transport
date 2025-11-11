package org.example.transport.mapper;

import org.example.transport.dto.DeliveryDTO;
import org.example.transport.entity.Delivery;
import org.example.transport.enums.DeliveryStatus;

/**
 * Mapper for Delivery entity and DTO
 */
public class DeliveryMapper {

    public static DeliveryDTO toDTO(Delivery delivery) {
        if (delivery == null) {
            return null;
        }
        DeliveryDTO dto = new DeliveryDTO();
        dto.setId(delivery.getId());
        dto.setCustomerId(delivery.getCustomer() != null ? delivery.getCustomer().getId() : null);
        dto.setCustomerName(delivery.getCustomer() != null ? delivery.getCustomer().getName() : null);
        dto.setSpecificAddress(delivery.getSpecificAddress());
        dto.setSpecificLatitude(delivery.getSpecificLatitude());
        dto.setSpecificLongitude(delivery.getSpecificLongitude());
        dto.setEffectiveAddress(delivery.getEffectiveAddress());
        dto.setEffectiveLatitude(delivery.getEffectiveLatitude());
        dto.setEffectiveLongitude(delivery.getEffectiveLongitude());
        dto.setWeightKg(delivery.getWeightKg());
        dto.setVolumeM3(delivery.getVolumeM3());
        dto.setPreferredTimeSlot(delivery.getPreferredTimeSlot());
        dto.setStatus(delivery.getStatus());
        dto.setTourId(delivery.getTour() != null ? delivery.getTour().getId() : null);
        dto.setSequenceInTour(delivery.getSequenceInTour());
        return dto;
    }

    public static Delivery toEntity(DeliveryDTO dto) {
        if (dto == null) {
            return null;
        }
        Delivery delivery = new Delivery();
        delivery.setId(dto.getId());
        // Customer must be set separately by the service
        delivery.setSpecificAddress(dto.getSpecificAddress());
        delivery.setSpecificLatitude(dto.getSpecificLatitude());
        delivery.setSpecificLongitude(dto.getSpecificLongitude());
        delivery.setWeightKg(dto.getWeightKg());
        delivery.setVolumeM3(dto.getVolumeM3());
        delivery.setPreferredTimeSlot(dto.getPreferredTimeSlot());
        delivery.setStatus(dto.getStatus() != null ? dto.getStatus() : DeliveryStatus.PENDING);
        delivery.setSequenceInTour(dto.getSequenceInTour());
        return delivery;
    }

    public static void updateEntityFromDTO(DeliveryDTO dto, Delivery delivery) {
        if (dto == null || delivery == null) {
            return;
        }
        // Customer update must be handled separately by the service
        delivery.setSpecificAddress(dto.getSpecificAddress());
        delivery.setSpecificLatitude(dto.getSpecificLatitude());
        delivery.setSpecificLongitude(dto.getSpecificLongitude());
        delivery.setWeightKg(dto.getWeightKg());
        delivery.setVolumeM3(dto.getVolumeM3());
        delivery.setPreferredTimeSlot(dto.getPreferredTimeSlot());
        if (dto.getStatus() != null) {
            delivery.setStatus(dto.getStatus());
        }
    }
}
