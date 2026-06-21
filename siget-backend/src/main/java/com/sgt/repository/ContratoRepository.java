package com.sgt.repository;

import com.sgt.entity.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContratoRepository extends JpaRepository<Contrato, Long>{
    
     // Busca contratos ativos
    List<Contrato> findByAtivoTrue();

    // Busca contratos de um órgão específico
    List<Contrato> findByOrgaoId(Long orgaoId);

    // Busca contratos ativos de um órgão
    List<Contrato> findByOrgaoIdAndAtivoTrue(Long orgaoId);

    // Verifica se número de contrato já existe
    boolean existsByNumero(String numero);
}
