package com.example.demo_adv.model.DTOs;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessoRequestDTO {
    private String numeroProcesso;
    private String titulo;
    private String descricao;
    private String tribunal;
    private String vara;
    private String status;
    private List<UUID> assistentesDesignadosIds;
}
