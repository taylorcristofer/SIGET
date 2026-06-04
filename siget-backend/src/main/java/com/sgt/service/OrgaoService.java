package com.sgt.service;

import com.sgt.dto.request.OrgaoRequest;
import com.sgt.dto.response.OrgaoResponse;
import com.sgt.entity.Orgao;
import com.sgt.repository.OrgaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrgaoService {
    
    private final OrgaoRepository repository;

    public List<OrgaoResponse> listarAtivos() {
        return repository.findByAtivoTrue()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<OrgaoResponse> listarTodos() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public OrgaoResponse buscarPorId(Long id) {
        Orgao o = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Órgão não encontrado"));
        return toResponse(o);
    }

    public OrgaoResponse cadastrar(OrgaoRequest request) {
        if (request.getCnpj() != null && repository.existsByCnpj(request.getCnpj())) {
            throw new RuntimeException("CNPJ já cadastrado");
        }
        return toResponse(repository.save(toEntity(request)));
    }

    public OrgaoResponse atualizar(Long id, OrgaoRequest request) {
        Orgao o = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Órgão não encontrado"));

        o.setNome(request.getNome());
        o.setSigla(request.getSigla());
        o.setCnpj(request.getCnpj());
        o.setEndereco(request.getEndereco());
        o.setCidade(request.getCidade());
        o.setContatoNome(request.getContatoNome());
        o.setContatoTel(request.getContatoTel());

        return toResponse(repository.save(o));
    }

    public void desativar(Long id) {
        Orgao o = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Órgão não encontrado"));
        o.setAtivo(false);
        repository.save(o);
    }

    private OrgaoResponse toResponse(Orgao o) {
        return OrgaoResponse.builder()
                .id(o.getId())
                .nome(o.getNome())
                .sigla(o.getSigla())
                .cnpj(o.getCnpj())
                .endereco(o.getEndereco())
                .cidade(o.getCidade())
                .contatoNome(o.getContatoNome())
                .contatoTel(o.getContatoTel())
                .ativo(o.getAtivo())
                .build();
    }

    private Orgao toEntity(OrgaoRequest request) {
        return Orgao.builder()
                .nome(request.getNome())
                .sigla(request.getSigla())
                .cnpj(request.getCnpj())
                .endereco(request.getEndereco())
                .cidade(request.getCidade())
                .contatoNome(request.getContatoNome())
                .contatoTel(request.getContatoTel())
                .build();
    }
}
