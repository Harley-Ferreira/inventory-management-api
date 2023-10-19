package com.harley.inventorymanagementapi.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @NotNull
    @Column(name = "street")
    private String street;

    @Column(name = "number_")
    private Integer number;

    @NotNull
    @Column(name = "district")
    private String district;

    @NotNull
    @Column(name = "city")
    private String city;

    @NotNull
    @Column(name = "state_")
    private String state;

    @NotNull
    @Column(name = "postal_code")
    private String postalCode;

    @NotNull
    @Column(name = "country")
    private String country;

    @NotNull
    @Column(name = "complement")
    private String complement;

}
