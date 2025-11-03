package org.example.transport.optimizer;

import org.example.transport.entity.Delivery;
import org.example.transport.entity.Tour;
import org.example.transport.util.DistanceCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Clarke-Wright Savings algorithm implementation for tour optimization
 * Calculates savings for merging routes and optimizes based on maximum savings
 */
@Component
public class ClarkeWrightOptimizer implements TourOptimizer {

    private static final Logger logger = LoggerFactory.getLogger(ClarkeWrightOptimizer.class);

    @Override
    public List<Delivery> calculateOptimalTour(Tour tour) {
        logger.info("Starting Clarke-Wright optimization for tour {}", tour.getId());
        
        List<Delivery> deliveries = new ArrayList<>(tour.getDeliveries());
        if (deliveries.isEmpty()) {
            logger.warn("No deliveries to optimize for tour {}", tour.getId());
            return deliveries;
        }

        if (deliveries.size() == 1) {
            return deliveries;
        }

        double warehouseLat = tour.getWarehouse().getLatitude();
        double warehouseLon = tour.getWarehouse().getLongitude();

        // Calculate savings for all pairs
        List<Saving> savings = new ArrayList<>();
        for (int i = 0; i < deliveries.size(); i++) {
            for (int j = i + 1; j < deliveries.size(); j++) {
                Delivery d1 = deliveries.get(i);
                Delivery d2 = deliveries.get(j);

                double distWarehouseToD1 = DistanceCalculator.calculateDistance(
                        warehouseLat, warehouseLon, d1.getLatitude(), d1.getLongitude());
                double distWarehouseToD2 = DistanceCalculator.calculateDistance(
                        warehouseLat, warehouseLon, d2.getLatitude(), d2.getLongitude());
                double distD1ToD2 = DistanceCalculator.calculateDistance(
                        d1.getLatitude(), d1.getLongitude(), d2.getLatitude(), d2.getLongitude());

                // Savings = distance(warehouse, d1) + distance(warehouse, d2) - distance(d1, d2)
                double savingValue = distWarehouseToD1 + distWarehouseToD2 - distD1ToD2;
                savings.add(new Saving(d1, d2, savingValue));
            }
        }

        // Sort savings in descending order
        savings.sort((s1, s2) -> Double.compare(s2.saving, s1.saving));

        // Build routes using savings
        List<Route> routes = new ArrayList<>();
        for (Delivery delivery : deliveries) {
            Route route = new Route();
            route.deliveries.add(delivery);
            routes.add(route);
        }

        // Merge routes based on savings
        for (Saving saving : savings) {
            Route route1 = findRouteContaining(routes, saving.delivery1);
            Route route2 = findRouteContaining(routes, saving.delivery2);

            if (route1 != null && route2 != null && route1 != route2) {
                // Check if deliveries are at the ends of their routes
                if (canMerge(route1, route2, saving.delivery1, saving.delivery2)) {
                    mergeRoutes(routes, route1, route2, saving.delivery1, saving.delivery2);
                }
            }
        }

        // Combine all routes into one optimized tour
        List<Delivery> optimizedRoute = routes.stream()
                .flatMap(route -> route.deliveries.stream())
                .collect(Collectors.toList());

        logger.info("Clarke-Wright optimization completed for tour {} with {} deliveries", 
                    tour.getId(), optimizedRoute.size());
        return optimizedRoute;
    }

    private Route findRouteContaining(List<Route> routes, Delivery delivery) {
        return routes.stream()
                .filter(route -> route.deliveries.contains(delivery))
                .findFirst()
                .orElse(null);
    }

    private boolean canMerge(Route route1, Route route2, Delivery d1, Delivery d2) {
        int index1 = route1.deliveries.indexOf(d1);
        int index2 = route2.deliveries.indexOf(d2);

        // Can merge if deliveries are at the ends of their routes
        return (index1 == 0 || index1 == route1.deliveries.size() - 1) &&
               (index2 == 0 || index2 == route2.deliveries.size() - 1);
    }

    private void mergeRoutes(List<Route> routes, Route route1, Route route2, 
                            Delivery d1, Delivery d2) {
        Route merged = new Route();
        
        int index1 = route1.deliveries.indexOf(d1);
        int index2 = route2.deliveries.indexOf(d2);

        // Determine merge order
        if (index1 == route1.deliveries.size() - 1 && index2 == 0) {
            merged.deliveries.addAll(route1.deliveries);
            merged.deliveries.addAll(route2.deliveries);
        } else if (index1 == 0 && index2 == route2.deliveries.size() - 1) {
            merged.deliveries.addAll(route2.deliveries);
            merged.deliveries.addAll(route1.deliveries);
        } else if (index1 == route1.deliveries.size() - 1 && index2 == route2.deliveries.size() - 1) {
            merged.deliveries.addAll(route1.deliveries);
            List<Delivery> reversed = new ArrayList<>(route2.deliveries);
            Collections.reverse(reversed);
            merged.deliveries.addAll(reversed);
        } else if (index1 == 0 && index2 == 0) {
            List<Delivery> reversed = new ArrayList<>(route1.deliveries);
            Collections.reverse(reversed);
            merged.deliveries.addAll(reversed);
            merged.deliveries.addAll(route2.deliveries);
        }

        routes.remove(route1);
        routes.remove(route2);
        routes.add(merged);
    }

    /**
     * Inner class representing a saving between two deliveries
     */
    private static class Saving {
        Delivery delivery1;
        Delivery delivery2;
        double saving;

        Saving(Delivery delivery1, Delivery delivery2, double saving) {
            this.delivery1 = delivery1;
            this.delivery2 = delivery2;
            this.saving = saving;
        }
    }

    /**
     * Inner class representing a route
     */
    private static class Route {
        List<Delivery> deliveries = new ArrayList<>();
    }
}
