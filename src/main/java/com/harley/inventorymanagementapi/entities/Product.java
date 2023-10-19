package com.harley.inventorymanagementapi.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotNull
    @Column(name = "company_name")
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "price")
    private double price;

    @NotNull
    @Column(name = "category")
    private String category;

    @NotNull
    @Column(name = "minimum_stock")
    private int minimumStock;
}
