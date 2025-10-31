package org.example.transport.controller;

import org.example.transport.dto.OptimizedTourResponseDTO;
import org.example.transport.dto.TourDTO;
import org.example.transport.enums.OptimizationAlgorithm;
import org.example.transport.service.TourService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST Controller for Tour operations
 */
@RestController
@RequestMapping("/api/tours")
public class TourController {

    private final TourService tourService;

    public TourController(TourService tourService) {
        this.tourService = tourService;
    }

    @GetMapping
    public ResponseEntity<List<TourDTO>> getAllTours() {
        return ResponseEntity.ok(tourService.getAllTours());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TourDTO> getTourById(@PathVariable Long id) {
        return ResponseEntity.ok(tourService.getTourById(id));
    }

    @PostMapping
    public ResponseEntity<TourDTO> createTour(@RequestBody TourDTO tourDTO) {
        TourDTO created = tourService.createTour(tourDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTour(@PathVariable Long id) {
        tourService.deleteTour(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{tourId}/deliveries/{deliveryId}")
    public ResponseEntity<TourDTO> addDeliveryToTour(
            @PathVariable Long tourId,
            @PathVariable Long deliveryId) {
        return ResponseEntity.ok(tourService.addDeliveryToTour(tourId, deliveryId));
    }

    @GetMapping("/{id}/optimize")
    public ResponseEntity<OptimizedTourResponseDTO> getOptimizedTour(
            @PathVariable Long id,
            @RequestParam OptimizationAlgorithm algorithm) {
        return ResponseEntity.ok(tourService.getOptimizedTour(id, algorithm));
    }

    @GetMapping("/{id}/distance")
    public ResponseEntity<Double> getTotalDistance(@PathVariable Long id) {
        return ResponseEntity.ok(tourService.getTotalDistance(id));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<TourDTO>> getToursByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(tourService.getToursByDate(date));
    }

    @GetMapping("/statistics/average-distance")
    public ResponseEntity<Double> getAverageDistanceByAlgorithm(
            @RequestParam OptimizationAlgorithm algorithm) {
        return ResponseEntity.ok(tourService.getAverageDistanceByAlgorithm(algorithm));
    }
}
