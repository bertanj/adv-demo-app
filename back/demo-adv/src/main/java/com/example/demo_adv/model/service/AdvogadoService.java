package com.example.demo_adv.model.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo_adv.model.DTOs.AdvogadoRequestDTO;
import com.example.demo_adv.model.DTOs.AdvogadoResponseDTO;
import com.example.demo_adv.model.UserRole;
import com.example.demo_adv.model.entity.Advogado;
import com.example.demo_adv.model.repositores.AdvogadoRepository;

@Service
public class AdvogadoService {

    @Autowired
    private AdvogadoRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Cadastro público — sempre ADVOGADO
    public AdvogadoResponseDTO salvar(AdvogadoRequestDTO dto) {
        Advogado advogado = new Advogado();
        advogado.setCpf(dto.getCpf());
        advogado.setName(dto.getName());
        advogado.setUsername(dto.getUsername());
        advogado.setEmail(dto.getEmail());
        advogado.setPassword(passwordEncoder.encode(dto.getPassword()));
        advogado.setOab(dto.getOab());
        advogado.setRole(UserRole.ADVOGADO);
        
        Advogado saved = repository.save(advogado);
        return convertToResponseDTO(saved);
    }

    // Cadastro feito por ADMIN — pode definir role
    public AdvogadoResponseDTO salvarComoAdmin(AdvogadoRequestDTO dto) {
        Advogado advogado = new Advogado();
        advogado.setCpf(dto.getCpf());
        advogado.setName(dto.getName());
        advogado.setUsername(dto.getUsername());
        advogado.setEmail(dto.getEmail());
        advogado.setPassword(passwordEncoder.encode(dto.getPassword()));
        advogado.setOab(dto.getOab());
        advogado.setRole(UserRole.ADVOGADO);
        
        Advogado saved = repository.save(advogado);
        return convertToResponseDTO(saved);
    }

    // Método para Listar (usado no GET da Controller)
    public List<AdvogadoResponseDTO> listarTodos() {
        return repository.findAll().stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    // Método para Buscar por Email (usado no Login/CustomUserDetails)
    public Advogado buscarPorEmail(String email) {
        return repository.findByEmail(email).orElse(null);
    }

    // Método para Atualizar (usado no PUT da Controller)
    public AdvogadoResponseDTO atualizar(UUID id, AdvogadoRequestDTO dto) {
        return repository.findById(id).map(advogado -> {
            advogado.setName(dto.getName());
            advogado.setEmail(dto.getEmail());
            advogado.setOab(dto.getOab());
            advogado.setUsername(dto.getUsername());

            // Se a senha for enviada na atualização, precisa criptografar de novo
            if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
                advogado.setPassword(passwordEncoder.encode(dto.getPassword()));
            }

            Advogado saved = repository.save(advogado);
            return convertToResponseDTO(saved);
        }).orElseThrow(() -> new RuntimeException("Advogado não encontrado com o ID: " + id));
    }

    // Método para Deletar (usado no DELETE da Controller)
    public void deletar(UUID id) {
        repository.deleteById(id);
    }

    private AdvogadoResponseDTO convertToResponseDTO(Advogado advogado) {
        return new AdvogadoResponseDTO(
                advogado.getId(),
                advogado.getCpf(),
                advogado.getName(),
                advogado.getUsername(),
                advogado.getEmail(),
                advogado.getOab(),
                advogado.getRole().toString()
        );
    }
}