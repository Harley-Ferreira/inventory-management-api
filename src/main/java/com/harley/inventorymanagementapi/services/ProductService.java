package com.harley.inventorymanagementapi.services;

import com.harley.inventorymanagementapi.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Product register(Product product);

    Product update(Long id, Product product);

    Product getById(Long id);

    Page<Product> getPageProducts(Pageable pageable);

    void delete(Long id);
}
