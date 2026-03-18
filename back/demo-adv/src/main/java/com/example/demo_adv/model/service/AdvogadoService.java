package com.example.demo_adv.model.service;

import com.example.demo_adv.model.entity.Advogado;
import com.example.demo_adv.model.repositores.AdvogadoRepository;
import com.example.demo_adv.model.UserRole; // Certifique-se de que o Enum UserRole existe
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AdvogadoService {

    @Autowired
    private AdvogadoRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Método para Salvar (com Criptografia)
    public Advogado salvar(Advogado advogado) {
        // 1. Criptografa a senha antes de enviar para o banco
        String senhaCriptografada = passwordEncoder.encode(advogado.getPassword());
        advogado.setPassword(senhaCriptografada);

        // 2. Define o papel padrão como ADMIN para advogados
        if (advogado.getRole() == null) {
            advogado.setRole(UserRole.ADMIN);
        }

        return repository.save(advogado);
    }

    // Método para Listar (usado no GET da Controller)
    public List<Advogado> listarTodos() {
        return repository.findAll();
    }

    // Método para Buscar por Email (usado no Login/CustomUserDetails)
    public Advogado buscarPorEmail(String email) {
        return repository.findByEmail(email).orElse(null);
    }

    // Método para Atualizar (usado no PUT da Controller)
    public Advogado atualizar(UUID id, Advogado atualizado) {
        return repository.findById(id).map(advogado -> {
            advogado.setName(atualizado.getName());
            advogado.setEmail(atualizado.getEmail());
            advogado.setOab(atualizado.getOab());
            advogado.setUsername(atualizado.getUsername());

            // Se a senha for enviada na atualização, precisa criptografar de novo
            if (atualizado.getPassword() != null && !atualizado.getPassword().isEmpty()) {
                advogado.setPassword(passwordEncoder.encode(atualizado.getPassword()));
            }

            return repository.save(advogado);
        }).orElseThrow(() -> new RuntimeException("Advogado não encontrado com o ID: " + id));
    }

    // Método para Deletar (usado no DELETE da Controller)
    public void deletar(UUID id) {
        repository.deleteById(id);
    }
}