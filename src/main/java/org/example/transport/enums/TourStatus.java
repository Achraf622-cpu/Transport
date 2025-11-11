package org.example.transport.enums;

/**
 * Enum representing the status of a tour
 * V2.0 - New enum for tour lifecycle management
 */
public enum TourStatus {
    PENDING,      // Tour created but not started
    IN_PROGRESS,  // Tour is currently being executed
    COMPLETED,    // Tour has been completed
    CANCELLED     // Tour was cancelled
}
