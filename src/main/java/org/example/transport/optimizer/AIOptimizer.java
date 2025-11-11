package org.example.transport.optimizer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.transport.entity.Delivery;
import org.example.transport.entity.DeliveryHistory;
import org.example.transport.entity.Tour;
import org.example.transport.repository.DeliveryHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * AI-powered tour optimization using Spring AI and historical delivery data
 * V2.0 - Uses machine learning to analyze patterns and optimize routes
 */
@Component
@ConditionalOnProperty(name = "tour.optimizer.algorithm", havingValue = "AI")
public class AIOptimizer implements TourOptimizer {

    private static final Logger logger = LoggerFactory.getLogger(AIOptimizer.class);

    @Autowired(required = false)
    private ChatModel chatModel;

    @Autowired
    private DeliveryHistoryRepository deliveryHistoryRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<Delivery> calculateOptimalTour(Tour tour) {
        logger.info("Starting AI-powered optimization for tour {}", tour.getId());

        List<Delivery> deliveries = new ArrayList<>(tour.getDeliveries());
        if (deliveries.isEmpty()) {
            logger.warn("No deliveries to optimize for tour {}", tour.getId());
            return deliveries;
        }

        // Check if AI is available
        if (chatModel == null) {
            logger.warn("ChatModel not available, falling back to simple optimization");
            return fallbackOptimization(tour);
        }

        try {
            // Gather historical data
            DayOfWeek dayOfWeek = tour.getTourDate().getDayOfWeek();
            List<DeliveryHistory> historicalData = deliveryHistoryRepository
                    .findByDayOfWeek(dayOfWeek)
                    .stream()
                    .limit(50) // Limit to recent history
                    .collect(Collectors.toList());

            // Build the AI prompt
            String prompt = buildOptimizationPrompt(tour, deliveries, historicalData);
            
            logger.debug("Sending prompt to AI model");
            
            // Call AI model
            Prompt aiPrompt = new Prompt(new UserMessage(prompt));
            String aiResponse = chatModel.call(aiPrompt).getResult().getOutput().getContent();
            
            logger.debug("Received AI response: {}", aiResponse);

            // Parse AI response and reorder deliveries
            List<Delivery> optimizedRoute = parseAIResponse(aiResponse, deliveries);
            
            logger.info("AI optimization completed for tour {} with {} deliveries", 
                       tour.getId(), optimizedRoute.size());
            
            return optimizedRoute;

        } catch (Exception e) {
            logger.error("Error during AI optimization: {}", e.getMessage(), e);
            logger.info("Falling back to simple optimization");
            return fallbackOptimization(tour);
        }
    }

    /**
     * Build a comprehensive prompt for the AI model
     * The quality of the prompt directly impacts optimization results
     */
    private String buildOptimizationPrompt(Tour tour, List<Delivery> deliveries, List<DeliveryHistory> history) {
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("# DELIVERY ROUTE OPTIMIZATION TASK\n\n");
        prompt.append("You are an expert logistics optimizer. Your task is to determine the optimal delivery order.\n\n");
        
        // Context
        prompt.append("## CONTEXT\n");
        prompt.append("- Tour Date: ").append(tour.getTourDate()).append("\n");
        prompt.append("- Day of Week: ").append(tour.getTourDate().getDayOfWeek()).append("\n");
        prompt.append("- Vehicle Type: ").append(tour.getVehicle().getType()).append("\n");
        prompt.append("- Warehouse Location: (").append(tour.getWarehouse().getLatitude())
              .append(", ").append(tour.getWarehouse().getLongitude()).append(")\n\n");
        
        // Current deliveries
        prompt.append("## DELIVERIES TO OPTIMIZE\n");
        prompt.append("```json\n");
        try {
            List<Map<String, Object>> deliveryData = deliveries.stream().map(d -> {
                Map<String, Object> data = new HashMap<>();
                data.put("id", d.getId());
                data.put("customerName", d.getCustomer() != null ? d.getCustomer().getName() : "Unknown");
                data.put("address", d.getEffectiveAddress());
                data.put("latitude", d.getEffectiveLatitude());
                data.put("longitude", d.getEffectiveLongitude());
                data.put("weightKg", d.getWeightKg());
                data.put("volumeM3", d.getVolumeM3());
                data.put("preferredTimeSlot", d.getPreferredTimeSlot());
                return data;
            }).collect(Collectors.toList());
            prompt.append(objectMapper.writeValueAsString(deliveryData));
        } catch (JsonProcessingException e) {
            logger.error("Error serializing deliveries", e);
        }
        prompt.append("\n```\n\n");
        
        // Historical patterns
        if (!history.isEmpty()) {
            prompt.append("## HISTORICAL DELIVERY PATTERNS\n");
            prompt.append("Analysis of past deliveries on ").append(tour.getTourDate().getDayOfWeek()).append("s:\n");
            
            // Calculate average delays by time slot
            Map<String, Double> avgDelaysBySlot = history.stream()
                    .filter(h -> h.getPreferredTimeSlot() != null && h.getDelayMinutes() != null)
                    .collect(Collectors.groupingBy(
                            DeliveryHistory::getPreferredTimeSlot,
                            Collectors.averagingDouble(DeliveryHistory::getDelayMinutes)
                    ));
            
            prompt.append("Average delays by time slot:\n");
            avgDelaysBySlot.forEach((slot, delay) -> 
                prompt.append("- ").append(slot).append(": ").append(String.format("%.1f", delay)).append(" minutes\n")
            );
            prompt.append("\n");
        }
        
        // Optimization criteria
        prompt.append("## OPTIMIZATION CRITERIA (in order of priority)\n");
        prompt.append("1. **Customer Time Preferences**: Respect preferred time slots when possible\n");
        prompt.append("2. **Distance Minimization**: Reduce total travel distance\n");
        prompt.append("3. **Historical Patterns**: Consider past delivery performance\n");
        prompt.append("4. **Geographic Clustering**: Group nearby deliveries together\n");
        prompt.append("5. **Time Windows**: Avoid delays based on historical data\n\n");
        
        // Output format
        prompt.append("## REQUIRED OUTPUT FORMAT\n");
        prompt.append("Respond with ONLY a valid JSON object (no markdown, no explanations outside JSON):\n");
        prompt.append("```json\n");
        prompt.append("{\n");
        prompt.append("  \"optimizedOrder\": [1, 3, 2, 4, ...],  // Array of delivery IDs in optimal order\n");
        prompt.append("  \"recommendations\": [\n");
        prompt.append("    \"Reason 1 for this ordering\",\n");
        prompt.append("    \"Reason 2 for this ordering\"\n");
        prompt.append("  ],\n");
        prompt.append("  \"estimatedTotalDistanceKm\": 45.2,\n");
        prompt.append("  \"confidenceScore\": 0.85  // 0-1 scale\n");
        prompt.append("}\n");
        prompt.append("```\n");
        
        return prompt.toString();
    }

    /**
     * Parse the AI response and reorder deliveries accordingly
     */
    private List<Delivery> parseAIResponse(String aiResponse, List<Delivery> deliveries) {
        try {
            // Extract JSON from response (in case AI added markdown)
            String jsonResponse = aiResponse;
            if (aiResponse.contains("```json")) {
                int start = aiResponse.indexOf("```json") + 7;
                int end = aiResponse.lastIndexOf("```");
                jsonResponse = aiResponse.substring(start, end).trim();
            } else if (aiResponse.contains("```")) {
                int start = aiResponse.indexOf("```") + 3;
                int end = aiResponse.lastIndexOf("```");
                jsonResponse = aiResponse.substring(start, end).trim();
            }
            
            // Parse JSON response
            Map<String, Object> response = objectMapper.readValue(jsonResponse, Map.class);
            List<Integer> optimizedOrder = (List<Integer>) response.get("optimizedOrder");
            List<String> recommendations = (List<String>) response.get("recommendations");
            
            logger.info("AI Recommendations: {}", recommendations);
            
            // Reorder deliveries based on AI response
            Map<Long, Delivery> deliveryMap = deliveries.stream()
                    .collect(Collectors.toMap(Delivery::getId, d -> d));
            
            List<Delivery> optimizedDeliveries = new ArrayList<>();
            for (Integer deliveryId : optimizedOrder) {
                Delivery delivery = deliveryMap.get(deliveryId.longValue());
                if (delivery != null) {
                    optimizedDeliveries.add(delivery);
                }
            }
            
            // Add any missing deliveries at the end
            for (Delivery delivery : deliveries) {
                if (!optimizedDeliveries.contains(delivery)) {
                    optimizedDeliveries.add(delivery);
                }
            }
            
            return optimizedDeliveries;
            
        } catch (Exception e) {
            logger.error("Error parsing AI response: {}", e.getMessage(), e);
            return deliveries; // Return original order on error
        }
    }

    /**
     * Fallback to simple nearest neighbor if AI is unavailable
     */
    private List<Delivery> fallbackOptimization(Tour tour) {
        logger.info("Using fallback nearest neighbor optimization");
        List<Delivery> deliveries = new ArrayList<>(tour.getDeliveries());
        List<Delivery> optimizedRoute = new ArrayList<>();
        Set<Delivery> visited = new HashSet<>();

        double currentLat = tour.getWarehouse().getLatitude();
        double currentLon = tour.getWarehouse().getLongitude();

        while (visited.size() < deliveries.size()) {
            Delivery nearest = null;
            double minDistance = Double.MAX_VALUE;

            for (Delivery delivery : deliveries) {
                if (!visited.contains(delivery)) {
                    double distance = calculateDistance(
                            currentLat, currentLon,
                            delivery.getEffectiveLatitude(), delivery.getEffectiveLongitude()
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
                currentLat = nearest.getEffectiveLatitude();
                currentLon = nearest.getEffectiveLongitude();
            }
        }

        return optimizedRoute;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth's radius in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
