package org.example.transport.service;

import org.example.transport.dto.DeliveryDTO;
import org.example.transport.entity.Delivery;
import org.example.transport.enums.DeliveryStatus;
import org.example.transport.exception.ResourceNotFoundException;
import org.example.transport.mapper.DeliveryMapper;
import org.example.transport.repository.DeliveryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * for managing deliveries
 */
@Service
public class DeliveryService {

    private static final Logger logger = LoggerFactory.getLogger(DeliveryService.class);

    private final DeliveryRepository deliveryRepository;

    public DeliveryService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    public List<DeliveryDTO> getAllDeliveries() {
        logger.info("Fetching all deliveries");
        return deliveryRepository.findAll().stream()
                .map(DeliveryMapper::toDTO)
                .collect(Collectors.toList());
    }

    public DeliveryDTO getDeliveryById(Long id) {
        logger.info("Fetching delivery with id: {}", id);
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery", id));
        return DeliveryMapper.toDTO(delivery);
    }

    public DeliveryDTO createDelivery(DeliveryDTO deliveryDTO) {
        logger.info("Creating new delivery at: {}", deliveryDTO.getAddress());
        Delivery delivery = DeliveryMapper.toEntity(deliveryDTO);
        Delivery saved = deliveryRepository.save(delivery);
        logger.info("Delivery created with id: {}", saved.getId());
        return DeliveryMapper.toDTO(saved);
    }

    public DeliveryDTO updateDelivery(Long id, DeliveryDTO deliveryDTO) {
        logger.info("Updating delivery with id: {}", id);
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery", id));
        DeliveryMapper.updateEntityFromDTO(deliveryDTO, delivery);
        Delivery updated = deliveryRepository.save(delivery);
        logger.info("Delivery updated: {}", id);
        return DeliveryMapper.toDTO(updated);
    }

    public void deleteDelivery(Long id) {
        logger.info("Deleting delivery with id: {}", id);
        if (!deliveryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Delivery", id);
        }
        deliveryRepository.deleteById(id);
        logger.info("Delivery deleted: {}", id);
    }

    public List<DeliveryDTO> getDeliveriesByStatus(DeliveryStatus status) {
        logger.info("Fetching deliveries by status: {}", status);
        return deliveryRepository.findByStatus(status).stream()
                .map(DeliveryMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<DeliveryDTO> getUnassignedDeliveries() {
        logger.info("Fetching unassigned deliveries");
        return deliveryRepository.findUnassignedDeliveries().stream()
                .map(DeliveryMapper::toDTO)
                .collect(Collectors.toList());
    }

    public DeliveryDTO updateDeliveryStatus(Long id, DeliveryStatus status) {
        logger.info("Updating delivery {} status to: {}", id, status);
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery", id));
        delivery.setStatus(status);
        Delivery updated = deliveryRepository.save(delivery);
        logger.info("Delivery status updated: {}", id);
        return DeliveryMapper.toDTO(updated);
    }
}
