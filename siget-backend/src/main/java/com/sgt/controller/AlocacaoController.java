package com.sgt.controller;

import com.sgt.dto.request.AlocacaoRequest;
import com.sgt.dto.response.AlocacaoResponse;
import com.sgt.service.AlocacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alocacoes")
@RequiredArgsConstructor
public class AlocacaoController {
    
    private final AlocacaoService service;

    @GetMapping
    public ResponseEntity<List<AlocacaoResponse>> listarAtivas() {
        return ResponseEntity.ok(service.listarAtivas());
    }

    @GetMapping("/todas")
    public ResponseEntity<List<AlocacaoResponse>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlocacaoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/funcionario/{funcionarioId}")
    public ResponseEntity<List<AlocacaoResponse>> listarPorFuncionario(
            @PathVariable Long funcionarioId) {
        return ResponseEntity.ok(service.listarPorFuncionario(funcionarioId));
    }

    @GetMapping("/contrato/{contratoId}")
    public ResponseEntity<List<AlocacaoResponse>> listarPorContrato(
            @PathVariable Long contratoId) {
        return ResponseEntity.ok(service.listarPorContrato(contratoId));
    }

    @PostMapping
    public ResponseEntity<AlocacaoResponse> cadastrar(
            @Valid @RequestBody AlocacaoRequest request) {
        return ResponseEntity.status(201).body(service.cadastrar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlocacaoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AlocacaoRequest request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @PatchMapping("/{id}/encerrar")
    public ResponseEntity<Void> encerrar(@PathVariable Long id) {
        service.encerrar(id);
        return ResponseEntity.noContent().build();
    }
}
