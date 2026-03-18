package com.example.demo_adv.controller;

import com.example.demo_adv.model.entity.Assistente;
import com.example.demo_adv.model.repositores.AssitenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/assistentes")
@CrossOrigin(origins = "http://localhost:8081")
public class AssistenteController {

    @Autowired
    private AssitenteRepository repository; // Injetando a ferramenta de banco

    // 1. CREATE (Criar)
    @PostMapping
    public Assistente criar(@RequestBody Assistente assistente) {
        return repository.save(assistente);
    }

    // 2. READ (Listar todos)
    @GetMapping
    public List<Assistente> listar() {
        return repository.findAll();
    }

    // 3. READ (Buscar por ID específico)
    @GetMapping("/{id}")
    public Assistente buscarPorId(@PathVariable UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assistente não encontrado"));
    }

    // 4. UPDATE (Atualizar)
    @PutMapping("/{id}")
    public Assistente atualizar(@PathVariable UUID id, @RequestBody Assistente assistenteAtualizado) {
        return repository.findById(id).map(assistente -> {
            assistente.setName(assistenteAtualizado.getName());
            // Adicione aqui outros campos que o Assistente tiver (email, cpf, etc)
            // exemplo: assistente.setEmail(assistenteAtualizado.getEmail());
            return repository.save(assistente);
        }).orElseThrow(() -> new RuntimeException("Assistente não encontrado"));
    }

    // 5. DELETE (Remover)
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable UUID id) {
        repository.deleteById(id);
    }
}