package com.example.demo_adv.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity(name = "cliente")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, unique = true)
    private String cpf;
    private String name;
    private String username;
    private String email;
    private String password;

    @ManyToOne
    @JoinColumn(name = "advogado_id") // Cria a chave estrangeira no banco
    private Advogado advogado;
}
