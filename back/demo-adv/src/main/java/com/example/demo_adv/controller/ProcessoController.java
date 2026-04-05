package com.example.demo_adv.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo_adv.model.DTOs.ProcessoRequestDTO;
import com.example.demo_adv.model.DTOs.ProcessoResponseDTO;
import com.example.demo_adv.model.entity.Advogado;
import com.example.demo_adv.model.service.AdvogadoService;
import com.example.demo_adv.model.service.ProcessoService;

@RestController
@RequestMapping("/processos")
@CrossOrigin(origins = "http://localhost:8081")
public class ProcessoController {

    @Autowired
    private ProcessoService processoService;

    @Autowired
    private AdvogadoService advogadoService;

    @PostMapping
    public ResponseEntity<ProcessoResponseDTO> criar(@RequestBody ProcessoRequestDTO processo, Authentication authentication) {
        String email = authentication.getName();
        Advogado advogado = advogadoService.buscarPorEmail(email);
        if (advogado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        ProcessoResponseDTO response = processoService.salvar(processo, advogado);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<ProcessoResponseDTO> listar(Authentication authentication) {
        String email = authentication.getName();
        Advogado advogado = advogadoService.buscarPorEmail(email);
        if (advogado == null) {
            return List.of();
        }
        return processoService.listarPorAdvogado(advogado.getId());
    }

    @GetMapping("/{id}")
    public ProcessoResponseDTO buscarPorId(@PathVariable UUID id) {
        return processoService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public ProcessoResponseDTO atualizar(@PathVariable UUID id, @RequestBody ProcessoRequestDTO processoAtualizado) {
        return processoService.atualizar(id, processoAtualizado);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable UUID id) {
        processoService.deletar(id);
    }
}
