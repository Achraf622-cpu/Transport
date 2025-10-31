package org.example.transport.optimizer;

import org.example.transport.entity.Delivery;
import org.example.transport.entity.Tour;
import org.example.transport.util.DistanceCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Nearest Neighbor algorithm implementation for tour optimization
 * Always chooses the closest unvisited delivery
 */
public class NearestNeighborOptimizer implements TourOptimizer {

    private static final Logger logger = LoggerFactory.getLogger(NearestNeighborOptimizer.class);

    @Override
    public List<Delivery> calculateOptimalTour(Tour tour) {
        logger.info("Starting Nearest Neighbor optimization for tour {}", tour.getId());
        
        List<Delivery> deliveries = new ArrayList<>(tour.getDeliveries());
        if (deliveries.isEmpty()) {
            logger.warn("No deliveries to optimize for tour {}", tour.getId());
            return deliveries;
        }

        List<Delivery> optimizedRoute = new ArrayList<>();
        Set<Delivery> visited = new HashSet<>();

        // Start from warehouse
        double currentLat = tour.getWarehouse().getLatitude();
        double currentLon = tour.getWarehouse().getLongitude();

        // Visit all deliveries
        while (visited.size() < deliveries.size()) {
            Delivery nearest = null;
            double minDistance = Double.MAX_VALUE;

            // Find nearest unvisited delivery
            for (Delivery delivery : deliveries) {
                if (!visited.contains(delivery)) {
                    double distance = DistanceCalculator.calculateDistance(
                            currentLat, currentLon,
                            delivery.getLatitude(), delivery.getLongitude()
                    );
                    if (distance < minDistance) {
                        minDistance = distance;
                        nearest = delivery;
                    }
                }
            }

            if (nearest != null) {
                optimizedRoute.add(nearest);
                visited.add(nearest);
                currentLat = nearest.getLatitude();
                currentLon = nearest.getLongitude();
            }
        }

        logger.info("Nearest Neighbor optimization completed for tour {} with {} deliveries", 
                    tour.getId(), optimizedRoute.size());
        return optimizedRoute;
    }
}
