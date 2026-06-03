package com.sgt.service;

import com.sgt.dto.request.FuncionarioRequest;
import com.sgt.dto.response.FuncionarioResponse;
import com.sgt.entity.Funcionario;
import com.sgt.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FuncionarioService {
    
    private final FuncionarioRepository repository;

    public List<FuncionarioResponse> listarAtivos() {
        return repository.findByAtivoTrue()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<FuncionarioResponse> listarTodos() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public FuncionarioResponse buscarPorId(Long id) {
        Funcionario f = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));
        return toResponse(f);
    }

    public FuncionarioResponse cadastrar(FuncionarioRequest request) {
        if (repository.existsByCpf(request.getCpf())) {
            throw new RuntimeException("CPF já cadastrado");
        }
        Funcionario f = toEntity(request);
        return toResponse(repository.save(f));
    }

    public FuncionarioResponse atualizar(Long id, FuncionarioRequest request) {
        Funcionario f = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));

        f.setNome(request.getNome());
        f.setCpf(request.getCpf());
        f.setRg(request.getRg());
        f.setDataNascimento(request.getDataNascimento());
        f.setTelefone(request.getTelefone());
        f.setEmail(request.getEmail());
        f.setFuncao(request.getFuncao());
        f.setDataAdmissao(request.getDataAdmissao());
        f.setCamisa(request.getCamisa());
        f.setCalca(request.getCalca());
        f.setBota(request.getBota());

        return toResponse(repository.save(f));
    }

    public void desativar(Long id) {
        Funcionario f = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));
        f.setAtivo(false);
        f.setStatus("INATIVO");
        repository.save(f);
    }

    // Converte Entity → Response
    private FuncionarioResponse toResponse(Funcionario f) {
        return FuncionarioResponse.builder()
                .id(f.getId())
                .nome(f.getNome())
                .cpf(f.getCpf())
                .rg(f.getRg())
                .dataNascimento(f.getDataNascimento())
                .telefone(f.getTelefone())
                .email(f.getEmail())
                .funcao(f.getFuncao())
                .status(f.getStatus())
                .ativo(f.getAtivo())
                .dataAdmissao(f.getDataAdmissao())
                .criadoEm(f.getCriadoEm())
                .camisa(f.getCamisa())
                .calca(f.getCalca())
                .bota(f.getBota())
                .build();

    }

    // Converte Request → Entity
    private Funcionario toEntity(FuncionarioRequest request) {
        return Funcionario.builder()
                .nome(request.getNome())
                .cpf(request.getCpf())
                .rg(request.getRg())
                .dataNascimento(request.getDataNascimento())
                .telefone(request.getTelefone())
                .email(request.getEmail())
                .funcao(request.getFuncao())
                .dataAdmissao(request.getDataAdmissao())
                .camisa(request.getCamisa())
                .calca(request.getCalca())
                .bota(request.getBota())
                .build();
    }
}
