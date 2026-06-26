package com.sgt.repository;

import com.sgt.entity.ArquivoFuncionario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArquivoFuncionarioRepository extends JpaRepository<ArquivoFuncionario, Long> {
    List<ArquivoFuncionario> findByFuncionarioIdAndCategoriaAndAtivoTrue(
        Long funcionarioId, String categoria);
    List<ArquivoFuncionario> findByFuncionarioIdAndAtivoTrue(Long funcionarioId);
}