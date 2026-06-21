package com.sgt.service;

import com.sgt.dto.request.DependenteRequest;
import com.sgt.dto.response.DependenteResponse;
import com.sgt.entity.Dependente;
import com.sgt.entity.Funcionario;
import com.sgt.repository.DependenteRepository;
import com.sgt.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DependenteService {

    private final DependenteRepository dependenteRepository;
    private final FuncionarioRepository funcionarioRepository;

    public List<DependenteResponse> listarPorFuncionario(Long funcionarioId) {
        return dependenteRepository
            .findByFuncionarioIdAndAtivoTrue(funcionarioId)
            .stream()
            .map(DependenteResponse::from)
            .toList();
    }

    public DependenteResponse cadastrar(DependenteRequest request) {
        Funcionario funcionario = funcionarioRepository.findById(request.funcionarioId())
            .orElseThrow(() -> new RuntimeException(
                "Funcionário não encontrado: " + request.funcionarioId()));

        Dependente dependente = Dependente.builder()
            .funcionario(funcionario)
            .nome(request.nome())
            .parentesco(request.parentesco())
            .dataNascimento(request.dataNascimento())
            .cpf(request.cpf())
            .build();

        return DependenteResponse.from(dependenteRepository.save(dependente));
    }

    public DependenteResponse atualizar(Long id, DependenteRequest request) {
        Dependente dependente = buscarAtivo(id);

        dependente.setNome(request.nome());
        dependente.setParentesco(request.parentesco());
        dependente.setDataNascimento(request.dataNascimento());
        dependente.setCpf(request.cpf());

        return DependenteResponse.from(dependenteRepository.save(dependente));
    }

    public void desativar(Long id) {
        Dependente dependente = buscarAtivo(id);
        dependente.setAtivo(false);
        dependenteRepository.save(dependente);
    }

    private Dependente buscarAtivo(Long id) {
        return dependenteRepository.findById(id)
            .filter(Dependente::getAtivo)
            .orElseThrow(() -> new RuntimeException("Dependente não encontrado: " + id));
    }
}