package com.sgt.repository;

import com.sgt.entity.Orgao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrgaoRepository extends JpaRepository<Orgao, Long> {
    
     // Busca apenas órgãos ativos
    List<Orgao> findByAtivoTrue();

    // Busca por CNPJ
    Optional<Orgao> findByCnpj(String cnpj);

    // Verifica se CNPJ já existe
    boolean existsByCnpj(String cnpj);
}
