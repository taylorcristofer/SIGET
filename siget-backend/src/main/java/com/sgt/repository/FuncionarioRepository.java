package com.sgt.repository;

import com.sgt.entity.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    //Buscar apenas funcionários ativos
    List<Funcionario> findByAtivoTrue();

    //Buscar por CPF
    Optional<Funcionario> findByCpf(String cpf);

    //Verifica se o CPF já existe
    boolean existsByCpf(String cpf);

    //Busca por função
    List<Funcionario> findByFuncaoAndAtivoTrue(String funcao);
}