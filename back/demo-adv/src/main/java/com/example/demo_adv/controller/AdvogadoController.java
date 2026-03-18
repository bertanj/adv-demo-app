package com.example.demo_adv.controller;

import com.example.demo_adv.model.entity.Advogado;
import com.example.demo_adv.model.service.AdvogadoService; // Importe a Service agora
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/advogados")
public class AdvogadoController {

    @Autowired
    private AdvogadoService advogadoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Advogado criarAdvogado(@RequestBody Advogado advogado) {
        // Agora o 'salvar' da Service será executado, criptografando a senha
        return advogadoService.salvar(advogado);
    }

    @GetMapping
    public List<Advogado> listarAdvogados() {
        // Você pode criar esse método na Service também para manter o padrão
        return advogadoService.listarTodos();
    }

    @PutMapping("/{id}")
    public Advogado atualizar(@PathVariable UUID id, @RequestBody Advogado advogadoAtualizado) {
        // Idealmente, a lógica de atualização também deve ir para a Service
        return advogadoService.atualizar(id, advogadoAtualizado);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable UUID id) {
        advogadoService.deletar(id);
    }
}