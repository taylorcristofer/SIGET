package com.sgt.repository;

import com.sgt.entity.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

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