package org.example.transport.exception;

/**
 * Exception thrown when vehicle capacity is exceeded
 */
public class VehicleCapacityExceededException extends RuntimeException {
    
    public VehicleCapacityExceededException(String message) {
        super(message);
    }
}
