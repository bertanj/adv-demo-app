package com.example.demo_adv.model.DTOs;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessoRequestDTO {
    @NotBlank(message = "Número do processo é obrigatório")
    private String numeroProcesso;
    
    @NotBlank(message = "Título é obrigatório")
    private String titulo;
    
    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;
    
    @NotBlank(message = "Tribunal é obrigatório")
    private String tribunal;
    
    @NotBlank(message = "Vara é obrigatória")
    private String vara;
    
    @NotBlank(message = "Status é obrigatório")
    private String status;
    
    private List<UUID> assistentesDesignadosIds;
}
