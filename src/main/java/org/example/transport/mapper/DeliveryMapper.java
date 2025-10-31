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
        return new DeliveryDTO(
                delivery.getId(),
                delivery.getAddress(),
                delivery.getLatitude(),
                delivery.getLongitude(),
                delivery.getWeightKg(),
                delivery.getVolumeM3(),
                delivery.getPreferredTimeSlot(),
                delivery.getStatus(),
                delivery.getTour() != null ? delivery.getTour().getId() : null,
                delivery.getSequenceInTour()
        );
    }

    public static Delivery toEntity(DeliveryDTO dto) {
        if (dto == null) {
            return null;
        }
        Delivery delivery = new Delivery();
        delivery.setId(dto.getId());
        delivery.setAddress(dto.getAddress());
        delivery.setLatitude(dto.getLatitude());
        delivery.setLongitude(dto.getLongitude());
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
        delivery.setAddress(dto.getAddress());
        delivery.setLatitude(dto.getLatitude());
        delivery.setLongitude(dto.getLongitude());
        delivery.setWeightKg(dto.getWeightKg());
        delivery.setVolumeM3(dto.getVolumeM3());
        delivery.setPreferredTimeSlot(dto.getPreferredTimeSlot());
        if (dto.getStatus() != null) {
            delivery.setStatus(dto.getStatus());
        }
    }
}
