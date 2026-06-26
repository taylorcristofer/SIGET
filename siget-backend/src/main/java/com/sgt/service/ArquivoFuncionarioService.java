package com.sgt.service;

import com.sgt.dto.request.ArquivoFuncionarioRequest;
import com.sgt.dto.response.ArquivoFuncionarioResponse;
import com.sgt.entity.ArquivoFuncionario;
import com.sgt.entity.Funcionario;
import com.sgt.repository.ArquivoFuncionarioRepository;
import com.sgt.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArquivoFuncionarioService {

    private final ArquivoFuncionarioRepository arquivoRepository;
    private final FuncionarioRepository funcionarioRepository;

    public List<ArquivoFuncionarioResponse> listarPorFuncionario(Long funcionarioId) {
        return arquivoRepository
            .findByFuncionarioIdAndAtivoTrue(funcionarioId)
            .stream()
            .map(ArquivoFuncionarioResponse::from)
            .toList();
    }

    public List<ArquivoFuncionarioResponse> listarPorCategoria(
            Long funcionarioId, String categoria) {
        return arquivoRepository
            .findByFuncionarioIdAndCategoriaAndAtivoTrue(
                funcionarioId, categoria.toUpperCase())
            .stream()
            .map(ArquivoFuncionarioResponse::from)
            .toList();
    }

    // Cadastro sem S3 por enquanto — url e s3Key ficam nulos
    public ArquivoFuncionarioResponse cadastrar(ArquivoFuncionarioRequest request) {
        Funcionario funcionario = funcionarioRepository
            .findById(request.funcionarioId())
            .orElseThrow(() -> new RuntimeException(
                "Funcionário não encontrado: " + request.funcionarioId()));

        ArquivoFuncionario arquivo = ArquivoFuncionario.builder()
            .funcionario(funcionario)
            .categoria(request.categoria().toUpperCase())
            .nomeArquivo(request.nomeArquivo())
            .mesReferencia(request.mesReferencia())
            .build();

        return ArquivoFuncionarioResponse.from(arquivoRepository.save(arquivo));
    }

    public void desativar(Long id) {
        ArquivoFuncionario arquivo = arquivoRepository.findById(id)
            .filter(ArquivoFuncionario::getAtivo)
            .orElseThrow(() -> new RuntimeException("Arquivo não encontrado: " + id));
        arquivo.setAtivo(false);
        arquivoRepository.save(arquivo);
    }
}