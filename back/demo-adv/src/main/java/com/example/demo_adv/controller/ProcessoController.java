package com.example.demo_adv.controller;

import com.example.demo_adv.model.entity.Advogado;
import com.example.demo_adv.model.entity.Processo;
import com.example.demo_adv.model.repositores.ProcessoRepository;
import com.example.demo_adv.model.service.AdvogadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/processos")
@CrossOrigin(origins = "http://localhost:8081")
public class ProcessoController {

    @Autowired
    private ProcessoRepository repository;

    @Autowired
    private AdvogadoService advogadoService;

    @PostMapping
    public ResponseEntity<Processo> criar(@RequestBody Processo processo, Authentication authentication) {
        String email = authentication.getName();
        Advogado advogado = advogadoService.buscarPorEmail(email);
        if (advogado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        processo.setAdvogado(advogado);
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(processo));
    }

    @GetMapping
    public List<Processo> listar(Authentication authentication) {
        String email = authentication.getName();
        Advogado advogado = advogadoService.buscarPorEmail(email);
        if (advogado == null) {
            return List.of();
        }
        return repository.findByAdvogadoId(advogado.getId());
    }

    @GetMapping("/{id}")
    public Processo buscarPorId(@PathVariable UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Processo não encontrado"));
    }

    @PutMapping("/{id}")
    public Processo atualizar(@PathVariable UUID id, @RequestBody Processo processoAtualizado) {
        return repository.findById(id).map(processo -> {
            processo.setNumeroProcesso(processoAtualizado.getNumeroProcesso());
            processo.setTitulo(processoAtualizado.getTitulo());
            processo.setStatus(processoAtualizado.getStatus());
            processo.setDescricao(processoAtualizado.getDescricao());
            processo.setTribunal(processoAtualizado.getTribunal());
            processo.setVara(processoAtualizado.getVara());
            return repository.save(processo);
        }).orElseThrow(() -> new RuntimeException("Processo não encontrado"));
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable UUID id) {
        repository.deleteById(id);
    }
}
