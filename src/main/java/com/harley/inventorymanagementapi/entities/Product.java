package com.harley.inventorymanagementapi.entities;

import com.harley.inventorymanagementapi.dtos.product.ProductDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "product")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "code")
    private Long code;
    @NotNull
    @Column(name = "company_name")
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "price")
    private BigDecimal price;

    @NotNull
    @Column(name = "category")
    private String category;

    @NotNull
    @Column(name = "minimum_stock")
    private Integer minimumStock;

    public Product(ProductDTO productDTO) {
        this.id = productDTO.id();
        this.code = productDTO.code();
        this.name = productDTO.name();
        this.description = productDTO.description();
        this.price = productDTO.price();
        this.category = productDTO.category();
        this.minimumStock = productDTO.minimumStock();
    }
}
