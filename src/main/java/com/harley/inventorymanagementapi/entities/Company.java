package com.harley.inventorymanagementapi.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "company")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "company_name")
    private String name;

    @NotNull
    @Column(name = "cnpj")
    private String cnpj;

    @Embedded
    private Contact contact;

    @Embedded
    private Address address;
}
