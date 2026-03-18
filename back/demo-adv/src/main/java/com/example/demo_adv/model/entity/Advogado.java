package com.example.demo_adv.model.entity;

import com.example.demo_adv.model.UserRole;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity(name="advogado")
public class Advogado {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String cpf;

    private String name;
    private String username;
    private String email;
    private String password;
    private String oab;

    // Adicione isto aqui:
    @Enumerated(EnumType.STRING)
    private UserRole role;
}