package com.example.demo_adv.model.repositores;

import com.example.demo_adv.model.entity.Assistente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AssitenteRepository extends JpaRepository<Assistente, UUID> {
}
