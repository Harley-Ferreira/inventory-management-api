package com.harley.inventorymanagementapi.entities;

import com.harley.inventorymanagementapi.enums.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "transaction")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotNull
    @Column(name = "transaction_date")
    private LocalDate date;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;

    @Column(name = "quantity")
    private int quantity;

    @NotNull
    @JoinColumn(name = "product", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @NotNull
    @JoinColumn(name = "supplier", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Supplier supplier;
}
