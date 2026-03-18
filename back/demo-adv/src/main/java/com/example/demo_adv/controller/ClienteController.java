package com.example.demo_adv.controller;

import com.example.demo_adv.model.entity.Cliente;
import com.example.demo_adv.model.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "http://localhost:8081")
public class ClienteController {

    @Autowired
    private ClienteService service; // Única injeção necessária aqui

    // CREATE
    @PostMapping
    public Cliente criar(@RequestBody Cliente cliente) {
        return service.salvar(cliente);
    }

    // READ - Listar todos
    @GetMapping
    public List<Cliente> listar() {
        return service.listarTodos();
    }

    // READ - Buscar por ID
    @GetMapping("/{id}")
    public Cliente buscarPorId(@PathVariable UUID id) {
        return service.buscarPorId(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public Cliente atualizar(@PathVariable UUID id, @RequestBody Cliente clienteAtualizado) {
        return service.atualizar(id, clienteAtualizado);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable UUID id) {
        service.deletar(id);
    }
}