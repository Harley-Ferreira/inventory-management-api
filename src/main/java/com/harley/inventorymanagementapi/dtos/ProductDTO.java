package com.harley.inventorymanagementapi.dtos;

import com.harley.inventorymanagementapi.entities.Product;

import java.math.BigDecimal;

public record ProductDTO(Long id, Long code, String name, String description, BigDecimal price, String category, Integer minimumStock) {
    public ProductDTO(Product product) {
        this (
                product.getId(),
                product.getCode(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory(),
                product.getMinimumStock());
    }
}
