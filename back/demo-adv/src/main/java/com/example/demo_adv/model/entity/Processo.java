package com.example.demo_adv.model.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Entity(name = "processo")
public class Processo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String numeroProcesso; // Número CNJ

    private String titulo; // Ex: "Indenizatória - João vs Telefônica"

    @Column(columnDefinition = "TEXT")
    private String descricao; // Detalhes da causa

    private String tribunal; // Ex: TJPR, TRF4

    private String vara; // Ex: 2ª Vara Cível

    private String status;

    @ManyToMany
    @JoinTable(
            name = "processo_assistente", // O nome da tabela que será criada no Postgres
            joinColumns = @JoinColumn(name = "processo_id"), // Coluna com o ID do processo
            inverseJoinColumns = @JoinColumn(name = "assistente_id") // Coluna com o ID do assistente
    )
    private List<Assistente> assistentesDesignados;
}