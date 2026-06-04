package com.sgt.controller;

import com.sgt.dto.request.OrgaoRequest;
import com.sgt.dto.response.OrgaoResponse;
import com.sgt.service.OrgaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orgaos")
@RequiredArgsConstructor
public class OrgaoController {
    
     private final OrgaoService service;

    @GetMapping
    public ResponseEntity<List<OrgaoResponse>> listarAtivos() {
        return ResponseEntity.ok(service.listarAtivos());
    }

    @GetMapping("/todos")
    public ResponseEntity<List<OrgaoResponse>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrgaoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<OrgaoResponse> cadastrar(
            @Valid @RequestBody OrgaoRequest request) {
        return ResponseEntity.status(201).body(service.cadastrar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrgaoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody OrgaoRequest request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        service.desativar(id);
        return ResponseEntity.noContent().build();
    }
}
