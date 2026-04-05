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
public class ProcessoResponseDTO {
    private UUID id;
    private String numeroProcesso;
    private String titulo;
    private String descricao;
    private String tribunal;
    private String vara;
    private String status;
    private UUID advogadoId;
    private String advogadoName;
    private List<AssistenteResponseDTO> assistentesDesignados;
}
