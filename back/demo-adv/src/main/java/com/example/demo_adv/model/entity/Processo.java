package com.example.demo_adv.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @ManyToOne
    @JoinColumn(name = "advogado_id")
    @JsonIgnoreProperties({"password", "cpf", "username"})
    private Advogado advogado;

    @ManyToMany
    @JoinTable(
            name = "processo_assistente",
            joinColumns = @JoinColumn(name = "processo_id"),
            inverseJoinColumns = @JoinColumn(name = "assistente_id")
    )
    private List<Assistente> assistentesDesignados;
}