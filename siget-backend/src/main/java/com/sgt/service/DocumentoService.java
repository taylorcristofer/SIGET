package com.sgt.service;

import com.sgt.dto.request.DocumentoRequest;
import com.sgt.dto.response.DocumentoResponse;
import com.sgt.entity.Documento;
import com.sgt.entity.Funcionario;
import com.sgt.repository.DocumentoRepository;
import com.sgt.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentoService {

    private final DocumentoRepository documentoRepository;
    private final FuncionarioRepository funcionarioRepository;

    public List<DocumentoResponse> listarPorFuncionario(Long funcionarioId) {
        return documentoRepository
            .findByFuncionarioIdAndAtivoTrue(funcionarioId)
            .stream()
            .map(DocumentoResponse::from)
            .toList();
    }

    public List<DocumentoResponse> listarVencendo(int dias) {
        LocalDate hoje = LocalDate.now();
        LocalDate limite = hoje.plusDays(dias);
        return documentoRepository
            .findDocumentosVencendo(hoje, limite)
            .stream()
            .map(DocumentoResponse::from)
            .toList();
    }

    public List<DocumentoResponse> listarVencidos() {
        return documentoRepository
            .findDocumentosVencidos(LocalDate.now())
            .stream()
            .map(DocumentoResponse::from)
            .toList();
    }

    public DocumentoResponse cadastrar(DocumentoRequest request) {
        Funcionario funcionario = funcionarioRepository.findById(request.funcionarioId())
            .orElseThrow(() -> new RuntimeException(
                "Funcionário não encontrado: " + request.funcionarioId()));

        Documento documento = Documento.builder()
            .funcionario(funcionario)
            .tipo(request.tipo())
            .descricao(request.descricao())
            .dataEmissao(request.dataEmissao())
            .dataVencimento(request.dataVencimento())
            .alertar(request.alertar() != null ? request.alertar() : false)
            .build();

        return DocumentoResponse.from(documentoRepository.save(documento));
    }

    public DocumentoResponse atualizar(Long id, DocumentoRequest request) {
        Documento documento = buscarAtivo(id);

        documento.setTipo(request.tipo());
        documento.setDescricao(request.descricao());
        documento.setDataEmissao(request.dataEmissao());
        documento.setDataVencimento(request.dataVencimento());
        documento.setAlertar(request.alertar() != null ? request.alertar() : false);

        return DocumentoResponse.from(documentoRepository.save(documento));
    }

    public void desativar(Long id) {
        Documento documento = buscarAtivo(id);
        documento.setAtivo(false);
        documentoRepository.save(documento);
    }

    private Documento buscarAtivo(Long id) {
        return documentoRepository.findById(id)
            .filter(Documento::getAtivo)
            .orElseThrow(() -> new RuntimeException("Documento não encontrado: " + id));
    }
}