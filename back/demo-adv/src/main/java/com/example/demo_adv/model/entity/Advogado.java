package com.example.demo_adv.model.entity;

import java.util.UUID;

import com.example.demo_adv.model.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name="advogado")
public class Advogado {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String cpf;

    private String name;
    private String username;
    
    @Column(unique = true)
    private String email;
    
    private String password;
    private String oab;

    // Adicione isto aqui:
    @Enumerated(EnumType.STRING)
    private UserRole role;
}