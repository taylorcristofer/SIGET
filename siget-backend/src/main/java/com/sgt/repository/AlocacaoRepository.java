package com.sgt.repository;

import com.sgt.entity.Alocacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlocacaoRepository extends JpaRepository<Alocacao, Long>{

    // Busca alocações de um funcionário
    List<Alocacao> findByFuncionarioId(Long funcionarioId);

    // Busca alocações de um contrato
    List<Alocacao> findByContratoId(Long contratoId);

    // Busca alocações ativas de um funcionário
    List<Alocacao> findByFuncionarioIdAndStatus(Long funcionarioId, String status);

    // Busca alocações ativas
    List<Alocacao> findByStatus(String status);
}
