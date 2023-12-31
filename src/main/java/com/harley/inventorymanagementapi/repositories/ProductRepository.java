package com.harley.inventorymanagementapi.repositories;

import com.harley.inventorymanagementapi.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByCode(Long code);
}
