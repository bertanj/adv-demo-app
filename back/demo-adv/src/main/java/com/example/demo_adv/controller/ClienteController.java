package com.example.demo_adv.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo_adv.model.DTOs.ClienteRequestDTO;
import com.example.demo_adv.model.DTOs.ClienteResponseDTO;
import com.example.demo_adv.model.service.ClienteService;

@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "http://localhost:8081")
public class ClienteController {

    @Autowired
    private ClienteService service; // Única injeção necessária aqui

    // CREATE
    @PostMapping
    public ClienteResponseDTO criar(@RequestBody ClienteRequestDTO cliente) {
        return service.salvar(cliente);
    }

    // READ - Listar todos
    @GetMapping
    public List<ClienteResponseDTO> listar() {
        return service.listarTodos();
    }

    // READ - Buscar por ID
    @GetMapping("/{id}")
    public ClienteResponseDTO buscarPorId(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ClienteResponseDTO atualizar(@PathVariable UUID id, @RequestBody ClienteRequestDTO clienteAtualizado) {
        return service.atualizar(id, clienteAtualizado);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable UUID id) {
        service.deletar(id);
    }
}