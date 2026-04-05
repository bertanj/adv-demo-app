package com.example.demo_adv.model.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo_adv.model.DTOs.ClienteRequestDTO;
import com.example.demo_adv.model.DTOs.ClienteResponseDTO;
import com.example.demo_adv.model.entity.Advogado;
import com.example.demo_adv.model.entity.Cliente;
import com.example.demo_adv.model.repositores.AdvogadoRepository;
import com.example.demo_adv.model.repositores.ClienteRepository;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;
    
    @Autowired
    private AdvogadoRepository advogadoRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public ClienteResponseDTO salvar(ClienteRequestDTO dto) {
        if (repository.existsByCpf(dto.getCpf())) {
            throw new RuntimeException("CPF já cadastrado!");
        }
        
        Cliente cliente = new Cliente();
        cliente.setCpf(dto.getCpf());
        cliente.setName(dto.getName());
        cliente.setUsername(dto.getUsername());
        cliente.setEmail(dto.getEmail());
        cliente.setPassword(passwordEncoder.encode(dto.getPassword()));
        
        if (dto.getAdvogadoId() != null) {
            Advogado advogado = advogadoRepository.findById(dto.getAdvogadoId())
                    .orElseThrow(() -> new RuntimeException("Advogado não encontrado"));
            cliente.setAdvogado(advogado);
        }
        
        Cliente saved = repository.save(cliente);
        return convertToResponseDTO(saved);
    }

    public List<ClienteResponseDTO> listarTodos() {
        return repository.findAll().stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    public ClienteResponseDTO buscarPorId(UUID id) {
        return repository.findById(id)
                .map(this::convertToResponseDTO)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }

    public ClienteResponseDTO atualizar(UUID id, ClienteRequestDTO dto) {
        Cliente clienteExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        clienteExistente.setName(dto.getName());
        clienteExistente.setEmail(dto.getEmail());
        clienteExistente.setUsername(dto.getUsername());
        
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            clienteExistente.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        
        if (dto.getAdvogadoId() != null) {
            Advogado advogado = advogadoRepository.findById(dto.getAdvogadoId())
                    .orElseThrow(() -> new RuntimeException("Advogado não encontrado"));
            clienteExistente.setAdvogado(advogado);
        }

        Cliente saved = repository.save(clienteExistente);
        return convertToResponseDTO(saved);
    }

    public void deletar(UUID id) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        repository.delete(cliente);
    }
    
    private ClienteResponseDTO convertToResponseDTO(Cliente cliente) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(cliente.getId());
        dto.setCpf(cliente.getCpf());
        dto.setName(cliente.getName());
        dto.setUsername(cliente.getUsername());
        dto.setEmail(cliente.getEmail());
        
        if (cliente.getAdvogado() != null) {
            dto.setAdvogadoId(cliente.getAdvogado().getId());
            dto.setAdvogadoName(cliente.getAdvogado().getName());
        }
        
        return dto;
    }
}