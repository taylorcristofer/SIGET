package com.sgt.controller;

import com.sgt.dto.request.DependenteRequest;
import com.sgt.dto.response.DependenteResponse;
import com.sgt.service.DependenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dependentes")
@RequiredArgsConstructor
public class DependenteController {

    private final DependenteService dependenteService;

    @GetMapping("/funcionario/{funcionarioId}")
    public List<DependenteResponse> listarPorFuncionario(@PathVariable Long funcionarioId) {
        return dependenteService.listarPorFuncionario(funcionarioId);
    }

    @PostMapping
    public ResponseEntity<DependenteResponse> cadastrar(@RequestBody DependenteRequest request) {
        return ResponseEntity.status(201).body(dependenteService.cadastrar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DependenteResponse> atualizar(
            @PathVariable Long id,
            @RequestBody DependenteRequest request) {
        return ResponseEntity.ok(dependenteService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        dependenteService.desativar(id);
        return ResponseEntity.noContent().build();
    }
}