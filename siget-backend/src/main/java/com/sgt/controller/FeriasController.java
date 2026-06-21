package com.sgt.controller;

import com.sgt.dto.request.FeriasRequest;
import com.sgt.dto.response.FeriasResponse;
import com.sgt.service.FeriasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ferias")
@RequiredArgsConstructor
public class FeriasController {

    private final FeriasService feriasService;

    @GetMapping("/funcionario/{funcionarioId}")
    public List<FeriasResponse> listarPorFuncionario(@PathVariable Long funcionarioId) {
        return feriasService.listarPorFuncionario(funcionarioId);
    }

    @PostMapping
    public ResponseEntity<FeriasResponse> cadastrar(@RequestBody FeriasRequest request) {
        return ResponseEntity.status(201).body(feriasService.cadastrar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FeriasResponse> atualizar(
            @PathVariable Long id,
            @RequestBody FeriasRequest request) {
        return ResponseEntity.ok(feriasService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        feriasService.desativar(id);
        return ResponseEntity.noContent().build();
    }
}