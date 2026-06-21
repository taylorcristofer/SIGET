package com.sgt.controller;

import com.sgt.dto.request.DocumentoRequest;
import com.sgt.dto.response.DocumentoResponse;
import com.sgt.service.DocumentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documentos")
@RequiredArgsConstructor
public class DocumentoController {

    private final DocumentoService documentoService;

    @GetMapping("/funcionario/{funcionarioId}")
    public List<DocumentoResponse> listarPorFuncionario(@PathVariable Long funcionarioId) {
        return documentoService.listarPorFuncionario(funcionarioId);
    }

    @GetMapping("/vencendo")
    public List<DocumentoResponse> listarVencendo(
            @RequestParam(defaultValue = "30") int dias) {
        return documentoService.listarVencendo(dias);
    }

    @GetMapping("/vencidos")
    public List<DocumentoResponse> listarVencidos() {
        return documentoService.listarVencidos();
    }

    @PostMapping
    public ResponseEntity<DocumentoResponse> cadastrar(@RequestBody DocumentoRequest request) {
        return ResponseEntity.status(201).body(documentoService.cadastrar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentoResponse> atualizar(
            @PathVariable Long id,
            @RequestBody DocumentoRequest request) {
        return ResponseEntity.ok(documentoService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        documentoService.desativar(id);
        return ResponseEntity.noContent().build();
    }
}