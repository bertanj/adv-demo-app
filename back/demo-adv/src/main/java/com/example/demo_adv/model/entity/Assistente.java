package com.example.demo_adv.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;


@Data
@Entity(name= "assistente")
public class Assistente {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(length = 11, nullable = false)
    private String cpf;
    private String name;
    private String username;
    private String email;
    private String password;

    @ManyToOne
    @JoinColumn(name = "advogado_id")
    private Advogado advogadoChefe;
}
