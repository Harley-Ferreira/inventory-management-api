package com.harley.inventorymanagementapi.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_login")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_first_name")
    @NotBlank(message = "O nome deve ser inserido.")
    private String name;

    @Column(name = "user_last_name")
    @NotBlank(message = "O sobrenome deve ser inserido.")
    private String lastname;

    @Column
    @NotBlank(message = "O email deve ser inserido.")
    private Contact contact;

    @Column(name = "c_password")
    @NotBlank(message = "A senha deve ser inserida.")
    private String password;
}
