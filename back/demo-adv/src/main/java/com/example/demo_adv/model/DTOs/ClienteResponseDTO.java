package com.example.demo_adv.model.DTOs;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponseDTO {
    private UUID id;
    private String cpf;
    private String name;
    private String username;
    private String email;
    private UUID advogadoId;
    private String advogadoName;
}
