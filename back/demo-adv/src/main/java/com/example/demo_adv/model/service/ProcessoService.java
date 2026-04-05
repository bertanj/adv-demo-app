package com.example.demo_adv.model.service;

import com.example.demo_adv.model.entity.Assistente;
import com.example.demo_adv.model.entity.Processo;
import com.example.demo_adv.model.entity.Advogado;
import com.example.demo_adv.model.DTOs.ProcessoRequestDTO;
import com.example.demo_adv.model.DTOs.ProcessoResponseDTO;
import com.example.demo_adv.model.DTOs.AssistenteResponseDTO;
import com.example.demo_adv.model.repositores.ProcessoRepository;
import com.example.demo_adv.model.repositores.AssitenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProcessoService {

    @Autowired
    private ProcessoRepository processoRepository;

    @Autowired
    private AssitenteRepository assistenteRepository;

    public ProcessoResponseDTO salvar(ProcessoRequestDTO dto, Advogado advogado) {
        Processo processo = new Processo();
        processo.setNumeroProcesso(dto.getNumeroProcesso());
        processo.setTitulo(dto.getTitulo());
        processo.setDescricao(dto.getDescricao());
        processo.setTribunal(dto.getTribunal());
        processo.setVara(dto.getVara());
        processo.setStatus(dto.getStatus());
        processo.setAdvogado(advogado);

        if (dto.getAssistentesDesignadosIds() != null && !dto.getAssistentesDesignadosIds().isEmpty()) {
            List<Assistente> assistentes = assistenteRepository.findAllById(dto.getAssistentesDesignadosIds());
            processo.setAssistentesDesignados(assistentes);
        }

        Processo saved = processoRepository.save(processo);
        return convertToResponseDTO(saved);
    }

    public List<ProcessoResponseDTO> listarPorAdvogado(UUID advogadoId) {
        return processoRepository.findByAdvogadoId(advogadoId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public ProcessoResponseDTO buscarPorId(UUID id) {
        return processoRepository.findById(id)
                .map(this::convertToResponseDTO)
                .orElseThrow(() -> new RuntimeException("Processo não encontrado"));
    }

    public ProcessoResponseDTO atualizar(UUID id, ProcessoRequestDTO dto) {
        return processoRepository.findById(id).map(processo -> {
            processo.setNumeroProcesso(dto.getNumeroProcesso());
            processo.setTitulo(dto.getTitulo());
            processo.setStatus(dto.getStatus());
            processo.setDescricao(dto.getDescricao());
            processo.setTribunal(dto.getTribunal());
            processo.setVara(dto.getVara());

            if (dto.getAssistentesDesignadosIds() != null && !dto.getAssistentesDesignadosIds().isEmpty()) {
                List<Assistente> assistentes = assistenteRepository.findAllById(dto.getAssistentesDesignadosIds());
                processo.setAssistentesDesignados(assistentes);
            }

            Processo saved = processoRepository.save(processo);
            return convertToResponseDTO(saved);
        }).orElseThrow(() -> new RuntimeException("Processo não encontrado"));
    }

    public void deletar(UUID id) {
        processoRepository.deleteById(id);
    }

    private ProcessoResponseDTO convertToResponseDTO(Processo processo) {
        ProcessoResponseDTO dto = new ProcessoResponseDTO();
        dto.setId(processo.getId());
        dto.setNumeroProcesso(processo.getNumeroProcesso());
        dto.setTitulo(processo.getTitulo());
        dto.setDescricao(processo.getDescricao());
        dto.setTribunal(processo.getTribunal());
        dto.setVara(processo.getVara());
        dto.setStatus(processo.getStatus());

        if (processo.getAdvogado() != null) {
            dto.setAdvogadoId(processo.getAdvogado().getId());
            dto.setAdvogadoName(processo.getAdvogado().getName());
        }

        if (processo.getAssistentesDesignados() != null && !processo.getAssistentesDesignados().isEmpty()) {
            dto.setAssistentesDesignados(
                    processo.getAssistentesDesignados().stream()
                            .map(this::convertAssistenteToDTO)
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }

    private AssistenteResponseDTO convertAssistenteToDTO(Assistente assistente) {
        AssistenteResponseDTO dto = new AssistenteResponseDTO();
        dto.setId(assistente.getId());
        dto.setCpf(assistente.getCpf());
        dto.setName(assistente.getName());
        dto.setUsername(assistente.getUsername());
        dto.setEmail(assistente.getEmail());

        if (assistente.getAdvogadoChefe() != null) {
            dto.setAdvogadoChefeId(assistente.getAdvogadoChefe().getId());
            dto.setAdvogadoChefeNome(assistente.getAdvogadoChefe().getName());
        }

        return dto;
    }
}
