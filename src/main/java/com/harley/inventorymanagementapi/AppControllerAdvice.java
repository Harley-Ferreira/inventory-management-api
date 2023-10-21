package com.harley.inventorymanagementapi;

import com.harley.inventorymanagementapi.exceptions.ExistsObjectInDBException;
import com.harley.inventorymanagementapi.exceptions.ObjectNotFoundBDException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
public class AppControllerAdvice {

    @ExceptionHandler(ExistsObjectInDBException.class)
    public ResponseEntity handleExistsObjectInDBException(ExistsObjectInDBException e) {
        return ResponseEntity.badRequest().body(new ApiErrors(e));
    }

    @ExceptionHandler(ObjectNotFoundBDException.class)
    public ResponseEntity handleObjectNotFoundBDException(ObjectNotFoundBDException e) {
        return ResponseEntity.badRequest().body(new ApiErrors(e));
    }

    private record ApiErrors(List<String> errors) {
        private ApiErrors(Exception exception) {
            this(Arrays.asList(exception.getMessage()));
        }
    }
}
