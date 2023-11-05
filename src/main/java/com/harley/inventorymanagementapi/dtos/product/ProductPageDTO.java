package com.harley.inventorymanagementapi.dtos.product;

import com.harley.inventorymanagementapi.entities.Product;

import java.math.BigDecimal;

public record ProductPageDTO(Long id, Long code, String name, String category, BigDecimal price, Integer minimumStock) {
    public ProductPageDTO(Product product) {
        this (
                product.getId(),
                product.getCode(),
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                product.getMinimumStock());
    }
}
