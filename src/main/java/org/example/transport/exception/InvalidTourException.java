package org.example.transport.exception;

/**
 * Exception thrown when a tour is invalid
 */
public class InvalidTourException extends RuntimeException {
    
    public InvalidTourException(String message) {
        super(message);
    }
}
