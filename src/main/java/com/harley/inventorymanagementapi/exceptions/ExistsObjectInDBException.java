package com.harley.inventorymanagementapi.exceptions;

public class ExistsObjectInDBException extends RuntimeException {
    public ExistsObjectInDBException(String message) {
        super(message);
    }
}
