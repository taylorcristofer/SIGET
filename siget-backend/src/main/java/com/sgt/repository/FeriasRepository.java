package com.sgt.repository;

import com.sgt.entity.Ferias;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeriasRepository extends JpaRepository<Ferias, Long> {
    List<Ferias> findByFuncionarioIdAndAtivoTrue(Long funcionarioId);
}