package com.sgt.controller;

import com.sgt.dto.request.ContratoRequest;
import com.sgt.dto.response.ContratoResponse;
import com.sgt.service.ContratoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contratos")
@RequiredArgsConstructor
public class ContratoController {
    
     private final ContratoService service;

    @GetMapping
    public ResponseEntity<List<ContratoResponse>> listarAtivos() {
        return ResponseEntity.ok(service.listarAtivos());
    }

    @GetMapping("/todos")
    public ResponseEntity<List<ContratoResponse>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContratoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/orgao/{orgaoId}")
    public ResponseEntity<List<ContratoResponse>> listarPorOrgao(
            @PathVariable Long orgaoId) {
        return ResponseEntity.ok(service.listarPorOrgao(orgaoId));
    }

    @PostMapping
    public ResponseEntity<ContratoResponse> cadastrar(
            @Valid @RequestBody ContratoRequest request) {
        return ResponseEntity.status(201).body(service.cadastrar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContratoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ContratoRequest request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        service.desativar(id);
        return ResponseEntity.noContent().build();
    }
}
