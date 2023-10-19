package com.harley.inventorymanagementapi.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Contact {

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;
}
