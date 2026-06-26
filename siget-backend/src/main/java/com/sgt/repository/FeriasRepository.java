package com.sgt.repository;

import com.sgt.entity.Ferias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface FeriasRepository extends JpaRepository<Ferias, Long> {
    List<Ferias> findByFuncionarioIdAndAtivoTrue(Long funcionarioId);

    @Query("""
    SELECT f FROM Ferias f
    WHERE f.ativo = true
      AND f.dataInicio <= :hoje
      AND f.dataFim >= :hoje
""")
List<Ferias> findFuncionariosEmFerias(LocalDate hoje);
}