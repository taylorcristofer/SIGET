package com.sgt.service;

import com.sgt.dto.request.AlocacaoRequest;
import com.sgt.dto.response.AlocacaoResponse;
import com.sgt.entity.Alocacao;
import com.sgt.entity.Contrato;
import com.sgt.entity.Funcionario;
import com.sgt.repository.AlocacaoRepository;
import com.sgt.repository.ContratoRepository;
import com.sgt.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlocacaoService {
    
     private final AlocacaoRepository repository;
    private final FuncionarioRepository funcionarioRepository;
    private final ContratoRepository contratoRepository;

    public List<AlocacaoResponse> listarTodas() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<AlocacaoResponse> listarAtivas() {
        return repository.findByStatus("ATIVA")
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<AlocacaoResponse> listarPorFuncionario(Long funcionarioId) {
        return repository.findByFuncionarioId(funcionarioId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<AlocacaoResponse> listarPorContrato(Long contratoId) {
        return repository.findByContratoId(contratoId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public AlocacaoResponse buscarPorId(Long id) {
        Alocacao a = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alocação não encontrada"));
        return toResponse(a);
    }

    public AlocacaoResponse cadastrar(AlocacaoRequest request) {
        Funcionario funcionario = funcionarioRepository.findById(request.getFuncionarioId())
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));

        Contrato contrato = contratoRepository.findById(request.getContratoId())
                .orElseThrow(() -> new RuntimeException("Contrato não encontrado"));

        Alocacao a = toEntity(request, funcionario, contrato);
        return toResponse(repository.save(a));
    }

    public AlocacaoResponse atualizar(Long id, AlocacaoRequest request) {
        Alocacao a = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alocação não encontrada"));

        Funcionario funcionario = funcionarioRepository.findById(request.getFuncionarioId())
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));

        Contrato contrato = contratoRepository.findById(request.getContratoId())
                .orElseThrow(() -> new RuntimeException("Contrato não encontrado"));

        a.setFuncionario(funcionario);
        a.setContrato(contrato);
        a.setFuncaoAlocada(request.getFuncaoAlocada());
        a.setDataInicio(request.getDataInicio());
        a.setDataFim(request.getDataFim());
        a.setTurno(request.getTurno());
        a.setObservacoes(request.getObservacoes());

        return toResponse(repository.save(a));
    }

    public void encerrar(Long id) {
        Alocacao a = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alocação não encontrada"));
        a.setStatus("ENCERRADA");
        repository.save(a);
    }

    private AlocacaoResponse toResponse(Alocacao a) {
        return AlocacaoResponse.builder()
                .id(a.getId())
                .funcionarioId(a.getFuncionario().getId())
                .funcionarioNome(a.getFuncionario().getNome())
                .funcionarioCpf(a.getFuncionario().getCpf())
                .funcionarioFuncao(a.getFuncionario().getFuncao())
                .contratoId(a.getContrato().getId())
                .contratoNumero(a.getContrato().getNumero())
                .orgaoId(a.getContrato().getOrgao().getId())
                .orgaoNome(a.getContrato().getOrgao().getNome())
                .orgaoSigla(a.getContrato().getOrgao().getSigla())
                .funcaoAlocada(a.getFuncaoAlocada())
                .dataInicio(a.getDataInicio())
                .dataFim(a.getDataFim())
                .turno(a.getTurno())
                .status(a.getStatus())
                .observacoes(a.getObservacoes())
                .criadoEm(a.getCriadoEm())
                .build();
    }

    private Alocacao toEntity(AlocacaoRequest request, Funcionario funcionario, Contrato contrato) {
        return Alocacao.builder()
                .funcionario(funcionario)
                .contrato(contrato)
                .funcaoAlocada(request.getFuncaoAlocada())
                .dataInicio(request.getDataInicio())
                .dataFim(request.getDataFim())
                .turno(request.getTurno())
                .observacoes(request.getObservacoes())
                .build();
    }
}
