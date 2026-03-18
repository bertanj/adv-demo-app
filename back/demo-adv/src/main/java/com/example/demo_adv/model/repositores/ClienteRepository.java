package com.example.demo_adv.model.repositores;

import com.example.demo_adv.model.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID> {

    boolean existsByCpf(String cpf);
}
