package com.sgt.service;

import com.sgt.dto.request.ContratoRequest;
import com.sgt.dto.response.ContratoResponse;
import com.sgt.entity.Contrato;
import com.sgt.entity.Orgao;
import com.sgt.repository.ContratoRepository;
import com.sgt.repository.OrgaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContratoService {
    private final ContratoRepository repository;
    private final OrgaoRepository orgaoRepository;

    public List<ContratoResponse> listarAtivos() {
        return repository.findByAtivoTrue()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<ContratoResponse> listarTodos() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<ContratoResponse> listarPorOrgao(Long orgaoId) {
        return repository.findByOrgaoId(orgaoId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ContratoResponse buscarPorId(Long id) {
        Contrato c = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contrato não encontrado"));
        return toResponse(c);
    }

    public ContratoResponse cadastrar(ContratoRequest request) {
        if (repository.existsByNumero(request.getNumero())) {
            throw new RuntimeException("Número de contrato já cadastrado");
        }

        Orgao orgao = orgaoRepository.findById(request.getOrgaoId())
                .orElseThrow(() -> new RuntimeException("Órgão não encontrado"));

        Contrato c = toEntity(request, orgao);
        return toResponse(repository.save(c));
    }

    public ContratoResponse atualizar(Long id, ContratoRequest request) {
        Contrato c = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contrato não encontrado"));

        Orgao orgao = orgaoRepository.findById(request.getOrgaoId())
                .orElseThrow(() -> new RuntimeException("Órgão não encontrado"));

        c.setOrgao(orgao);
        c.setNumero(request.getNumero());
        c.setDataInicio(request.getDataInicio());
        c.setDataFim(request.getDataFim());
        c.setObjeto(request.getObjeto());
        c.setValorMensal(request.getValorMensal());

        return toResponse(repository.save(c));
    }

    public void desativar(Long id) {
        Contrato c = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contrato não encontrado"));
        c.setAtivo(false);
        repository.save(c);
    }

    private ContratoResponse toResponse(Contrato c) {
        return ContratoResponse.builder()
                .id(c.getId())
                .orgaoId(c.getOrgao().getId())
                .orgaoNome(c.getOrgao().getNome())
                .orgaoSigla(c.getOrgao().getSigla())
                .numero(c.getNumero())
                .dataInicio(c.getDataInicio())
                .dataFim(c.getDataFim())
                .objeto(c.getObjeto())
                .valorMensal(c.getValorMensal())
                .ativo(c.getAtivo())
                .build();
    }

    private Contrato toEntity(ContratoRequest request, Orgao orgao) {
        return Contrato.builder()
                .orgao(orgao)
                .numero(request.getNumero())
                .dataInicio(request.getDataInicio())
                .dataFim(request.getDataFim())
                .objeto(request.getObjeto())
                .valorMensal(request.getValorMensal())
                .build();
    }
}
