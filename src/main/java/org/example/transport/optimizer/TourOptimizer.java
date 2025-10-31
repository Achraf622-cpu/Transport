package org.example.transport.optimizer;

import org.example.transport.entity.Delivery;
import org.example.transport.entity.Tour;

import java.util.List;

/**
 * Interface for tour optimization algorithms
 */
public interface TourOptimizer {
    
    /**
     * Calculate the optimal tour for a list of deliveries
     * @param tour The tour containing deliveries to optimize
     * @return List of deliveries in optimal order
     */
    List<Delivery> calculateOptimalTour(Tour tour);
}
