package com.example.demo_adv.model.repositores;

import com.example.demo_adv.model.entity.Advogado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdvogadoRepository extends JpaRepository<Advogado, UUID> {
    Optional<Advogado> findByEmail(String email);
}
