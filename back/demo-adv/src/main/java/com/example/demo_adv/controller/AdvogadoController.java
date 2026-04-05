package com.example.demo_adv.controller;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo_adv.model.DTOs.AdvogadoRequestDTO;
import com.example.demo_adv.model.DTOs.AdvogadoResponseDTO;
import com.example.demo_adv.model.service.AdvogadoService;

@RestController
@RequestMapping("/advogados")
public class AdvogadoController {

    private static final Logger logger = LoggerFactory.getLogger(AdvogadoController.class);

    @Autowired
    private AdvogadoService advogadoService;

    // Cadastro público — sempre cria com role ADVOGADO
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdvogadoResponseDTO criarAdvogado(@RequestBody AdvogadoRequestDTO advogado) {
        return advogadoService.salvar(advogado);
    }

    // Cadastro feito por ADMIN — pode definir role
    @PostMapping("/admin")
    @ResponseStatus(HttpStatus.CREATED)
    public AdvogadoResponseDTO criarAdvogadoComoAdmin(@RequestBody AdvogadoRequestDTO advogado) {
        return advogadoService.salvarComoAdmin(advogado);
    }

    @GetMapping
    public List<AdvogadoResponseDTO> listarAdvogados() {
        logger.info("GET /advogados - Método executado com sucesso!");
        return advogadoService.listarTodos();
    }

    @PutMapping("/{id}")
    public AdvogadoResponseDTO atualizar(@PathVariable UUID id, @RequestBody AdvogadoRequestDTO advogadoAtualizado) {
        return advogadoService.atualizar(id, advogadoAtualizado);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable UUID id) {
        advogadoService.deletar(id);
    }
}