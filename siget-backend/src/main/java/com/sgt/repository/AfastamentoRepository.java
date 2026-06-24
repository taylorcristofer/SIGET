package com.sgt.repository;

import com.sgt.entity.Afastamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AfastamentoRepository extends JpaRepository<Afastamento, Long> {
    List<Afastamento> findByFuncionarioIdAndAtivoTrue(Long funcionarioId);
    List<Afastamento> findByTipoAndAtivoTrue(String tipo);

}