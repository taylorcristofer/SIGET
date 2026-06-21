
package com.sgt.repository;

import com.sgt.entity.Dependente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DependenteRepository extends JpaRepository<Dependente, Long> {
    List<Dependente> findByFuncionarioIdAndAtivoTrue(Long funcionarioId);
}