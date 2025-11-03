package org.example.transport.service;

import org.example.transport.dto.DeliveryDTO;
import org.example.transport.dto.OptimizedTourResponseDTO;
import org.example.transport.dto.TourDTO;
import org.example.transport.entity.Delivery;
import org.example.transport.entity.Tour;
import org.example.transport.entity.Vehicle;
import org.example.transport.entity.Warehouse;
import org.example.transport.enums.OptimizationAlgorithm;
import org.example.transport.exception.InvalidTourException;
import org.example.transport.exception.ResourceNotFoundException;
import org.example.transport.mapper.DeliveryMapper;
import org.example.transport.mapper.TourMapper;
import org.example.transport.optimizer.ClarkeWrightOptimizer;
import org.example.transport.optimizer.NearestNeighborOptimizer;
import org.example.transport.optimizer.TourOptimizer;
import org.example.transport.repository.DeliveryRepository;
import org.example.transport.repository.TourRepository;
import org.example.transport.repository.VehicleRepository;
import org.example.transport.repository.WarehouseRepository;
import org.example.transport.util.DistanceCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing tours and optimization
 */
@Service
public class TourService {

    private static final Logger logger = LoggerFactory.getLogger(TourService.class);

    private final TourRepository tourRepository;
    private final VehicleRepository vehicleRepository;
    private final WarehouseRepository warehouseRepository;
    private final DeliveryRepository deliveryRepository;
    private final NearestNeighborOptimizer nearestNeighborOptimizer;
    private final ClarkeWrightOptimizer clarkeWrightOptimizer;

    public TourService(TourRepository tourRepository,
                       VehicleRepository vehicleRepository,
                       WarehouseRepository warehouseRepository,
                       DeliveryRepository deliveryRepository,
                       NearestNeighborOptimizer nearestNeighborOptimizer,
                       ClarkeWrightOptimizer clarkeWrightOptimizer) {
        this.tourRepository = tourRepository;
        this.vehicleRepository = vehicleRepository;
        this.warehouseRepository = warehouseRepository;
        this.deliveryRepository = deliveryRepository;
        this.nearestNeighborOptimizer = nearestNeighborOptimizer;
        this.clarkeWrightOptimizer = clarkeWrightOptimizer;
    }

    public List<TourDTO> getAllTours() {
        logger.info("Fetching all tours");
        return tourRepository.findAll().stream()
                .map(TourMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TourDTO getTourById(Long id) {
        logger.info("Fetching tour with id: {}", id);
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tour", id));
        return TourMapper.toDTO(tour);
    }

    public TourDTO createTour(TourDTO tourDTO) {
        logger.info("Creating new tour for date: {}", tourDTO.getTourDate());
        
        Vehicle vehicle = vehicleRepository.findById(tourDTO.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle", tourDTO.getVehicleId()));
        
        Warehouse warehouse = warehouseRepository.findById(tourDTO.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse", tourDTO.getWarehouseId()));

        Tour tour = new Tour();
        tour.setTourDate(tourDTO.getTourDate());
        tour.setVehicle(vehicle);
        tour.setWarehouse(warehouse);
        tour.setOptimized(false);

        Tour saved = tourRepository.save(tour);
        logger.info("Tour created with id: {}", saved.getId());
        return TourMapper.toDTO(saved);
    }

    public void deleteTour(Long id) {
        logger.info("Deleting tour with id: {}", id);
        if (!tourRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tour", id);
        }
        tourRepository.deleteById(id);
        logger.info("Tour deleted: {}", id);
    }

    public TourDTO addDeliveryToTour(Long tourId, Long deliveryId) {
        logger.info("Adding delivery {} to tour {}", deliveryId, tourId);
        
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new ResourceNotFoundException("Tour", tourId));
        
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery", deliveryId));

        tour.addDelivery(delivery);
        tour.setOptimized(false);
        Tour updated = tourRepository.save(tour);
        
        logger.info("Delivery added to tour: {}", tourId);
        return TourMapper.toDTO(updated);
    }

    public OptimizedTourResponseDTO getOptimizedTour(Long tourId, OptimizationAlgorithm algorithm) {
        logger.info("Optimizing tour {} with algorithm: {}", tourId, algorithm);
        
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new ResourceNotFoundException("Tour", tourId));

        if (tour.getDeliveries().isEmpty()) {
            throw new InvalidTourException("Cannot optimize tour with no deliveries");
        }

        // Select optimizer based on algorithm
        TourOptimizer optimizer = algorithm == OptimizationAlgorithm.CLARKE_WRIGHT 
                ? clarkeWrightOptimizer 
                : nearestNeighborOptimizer;

        // Calculate optimal tour
        List<Delivery> optimizedDeliveries = optimizer.calculateOptimalTour(tour);

        // Update sequence numbers
        for (int i = 0; i < optimizedDeliveries.size(); i++) {
            optimizedDeliveries.get(i).setSequenceInTour(i + 1);
        }

        // Calculate total distance
        double totalDistance = getTotalDistance(tour, optimizedDeliveries);

        // Update tour
        tour.setAlgorithm(algorithm);
        tour.setTotalDistanceKm(totalDistance);
        tour.setTotalWeightKg(optimizedDeliveries.stream().mapToDouble(Delivery::getWeightKg).sum());
        tour.setTotalVolumeM3(optimizedDeliveries.stream().mapToDouble(Delivery::getVolumeM3).sum());
        tour.setOptimized(true);
        
        tourRepository.save(tour);

        // Create response
        List<DeliveryDTO> deliveryDTOs = optimizedDeliveries.stream()
                .map(DeliveryMapper::toDTO)
                .collect(Collectors.toList());

        logger.info("Tour optimized successfully. Total distance: {} km", totalDistance);
        
        return new OptimizedTourResponseDTO(
                tour.getId(),
                algorithm,
                deliveryDTOs,
                totalDistance,
                tour.getTotalWeightKg(),
                tour.getTotalVolumeM3()
        );
    }

    public Double getTotalDistance(Long tourId) {
        logger.info("Calculating total distance for tour: {}", tourId);
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new ResourceNotFoundException("Tour", tourId));
        
        List<Delivery> deliveries = deliveryRepository.findByTourIdOrderBySequence(tourId);
        return getTotalDistance(tour, deliveries);
    }

    private Double getTotalDistance(Tour tour, List<Delivery> deliveries) {
        if (deliveries.isEmpty()) {
            return 0.0;
        }

        double totalDistance = 0.0;
        double currentLat = tour.getWarehouse().getLatitude();
        double currentLon = tour.getWarehouse().getLongitude();

        // Distance from warehouse to first delivery
        Delivery firstDelivery = deliveries.get(0);
        totalDistance += DistanceCalculator.calculateDistance(
                currentLat, currentLon,
                firstDelivery.getLatitude(), firstDelivery.getLongitude()
        );

        // Distance between deliveries
        for (int i = 0; i < deliveries.size() - 1; i++) {
            Delivery current = deliveries.get(i);
            Delivery next = deliveries.get(i + 1);
            totalDistance += DistanceCalculator.calculateDistance(
                    current.getLatitude(), current.getLongitude(),
                    next.getLatitude(), next.getLongitude()
            );
        }

        // Distance from last delivery back to warehouse
        Delivery lastDelivery = deliveries.get(deliveries.size() - 1);
        totalDistance += DistanceCalculator.calculateDistance(
                lastDelivery.getLatitude(), lastDelivery.getLongitude(),
                tour.getWarehouse().getLatitude(), tour.getWarehouse().getLongitude()
        );

        return totalDistance;
    }

    public List<TourDTO> getToursByDate(LocalDate date) {
        logger.info("Fetching tours for date: {}", date);
        return tourRepository.findByTourDate(date).stream()
                .map(TourMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Double getAverageDistanceByAlgorithm(OptimizationAlgorithm algorithm) {
        logger.info("Calculating average distance for algorithm: {}", algorithm);
        Double average = tourRepository.getAverageDistanceByAlgorithm(algorithm);
        return average != null ? average : 0.0;
    }
}
