package org.example.transport.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for DistanceCalculator
 */
class DistanceCalculatorTest {

    @Test
    void testCalculateDistance_SameLocation() {
        double distance = DistanceCalculator.calculateDistance(48.8566, 2.3522, 48.8566, 2.3522);
        assertEquals(0.0, distance, 0.01);
    }

    @Test
    void testCalculateDistance_ParisToLondon() {
        // Paris coordinates
        double parisLat = 48.8566;
        double parisLon = 2.3522;
        
        // London coordinates
        double londonLat = 51.5074;
        double londonLon = -0.1278;
        
        double distance = DistanceCalculator.calculateDistance(parisLat, parisLon, londonLat, londonLon);
        
        // Expected distance is approximately 344 km
        assertTrue(distance > 340 && distance < 350, "Distance should be around 344 km");
    }

    @Test
    void testCalculateDistance_ShortDistance() {
        // Two points close to each other (approximately 1 km apart)
        double distance = DistanceCalculator.calculateDistance(48.8566, 2.3522, 48.8656, 2.3522);
        assertTrue(distance > 0 && distance < 2, "Distance should be less than 2 km");
    }
}
