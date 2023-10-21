package com.harley.inventorymanagementapi.exceptions;

public class ObjectNotFoundBDException extends RuntimeException {
    public ObjectNotFoundBDException(String message) {
        super(message);
    }
}
