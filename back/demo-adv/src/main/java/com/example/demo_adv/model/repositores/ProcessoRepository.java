package com.example.demo_adv.model.repositores;

import com.example.demo_adv.model.entity.Processo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProcessoRepository extends JpaRepository<Processo, UUID> {
    List<Processo> findByAdvogadoId(UUID advogadoId);
}
