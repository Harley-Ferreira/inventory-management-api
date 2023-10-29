package com.harley.inventorymanagementapi.factories;

import com.harley.inventorymanagementapi.dtos.product.ProductDTO;
import com.harley.inventorymanagementapi.entities.Product;

import java.math.BigDecimal;

public class ProductFactory {
    public static Product createNewProduct() {
        return new Product(1l, 33L, "Table", "Dining table", getValue(800.00), "Móveis", 9);
    }

    public static Product createNewProduct(Long id) {
        return new Product(id, 33L, "Table", "Dining table", getValue(800.00), "Móveis", 9);
    }

    public static ProductDTO createNewProductDTO() {
        return new ProductDTO(1l, 33L, "Table", "Dining table", getValue(800.00), "Móveis", 9);
    }

    public static ProductDTO createNewProductDTO(Long id) {
        return new ProductDTO(id, 33L, "Table", "Dining table", getValue(800.00), "Móveis", 9);
    }

    private static BigDecimal getValue(double v) {
        return new BigDecimal(v);
    }
}
