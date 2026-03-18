package com.example.demo_adv.controller;

import com.example.demo_adv.model.entity.Processo;
import com.example.demo_adv.model.repositores.ProcessoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/processos")
@CrossOrigin(origins = "http://localhost:8081")
public class ProcessoController {

    @Autowired
    private ProcessoRepository repository;

    // CREATE
    @PostMapping
    public Processo criar(@RequestBody Processo processo) {
        return repository.save(processo);
    }

    // READ - Todos
    @GetMapping
    public List<Processo> listar() {
        return repository.findAll();
    }

    // READ - Por ID
    @GetMapping("/{id}")
    public Processo buscarPorId(@PathVariable UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Processo não encontrado"));
    }

    // UPDATE
    @PutMapping("/{id}")
    public Processo atualizar(@PathVariable UUID id, @RequestBody Processo processoAtualizado) {
        return repository.findById(id).map(processo -> {
            processo.setNumeroProcesso(processoAtualizado.getNumeroProcesso());
            processo.setStatus(processoAtualizado.getStatus());
            processo.setDescricao(processoAtualizado.getDescricao());
            // Se houver datas ou valores, atualize aqui também
            return repository.save(processo);
        }).orElseThrow(() -> new RuntimeException("Processo não encontrado"));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable UUID id) {
        repository.deleteById(id);
    }
}