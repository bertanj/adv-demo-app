package com.example.demo_adv.model.service;

import com.example.demo_adv.model.entity.Cliente;
import com.example.demo_adv.model.repositores.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;

    public Cliente salvar(Cliente cliente) {
        if (repository.existsByCpf(cliente.getCpf())) {
            throw new RuntimeException("CPF já cadastrado!");
        }
        return repository.save(cliente);
    }

    public List<Cliente> listarTodos() {
        return repository.findAll();
    }

    public Cliente buscarPorId(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }

    public Cliente atualizar(UUID id, Cliente atualizado) {
        Cliente clienteExistente = buscarPorId(id);

        clienteExistente.setName(atualizado.getName());
        clienteExistente.setEmail(atualizado.getEmail());
        clienteExistente.setCpf(atualizado.getCpf());
        // Adicione outros campos se necessário

        return repository.save(clienteExistente);
    }

    public void deletar(UUID id) {
        Cliente cliente = buscarPorId(id);
        repository.delete(cliente);
    }
}