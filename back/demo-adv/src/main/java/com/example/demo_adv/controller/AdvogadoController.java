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

    // Cadastro público — sempre cria com role ADVOGADO
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Advogado criarAdvogado(@RequestBody Advogado advogado) {
        return advogadoService.salvar(advogado);
    }

    // Cadastro feito por ADMIN — pode definir role
    @PostMapping("/admin")
    @ResponseStatus(HttpStatus.CREATED)
    public Advogado criarAdvogadoComoAdmin(@RequestBody Advogado advogado) {
        return advogadoService.salvarComoAdmin(advogado);
    }

    @GetMapping
    public List<Advogado> listarAdvogados() {
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