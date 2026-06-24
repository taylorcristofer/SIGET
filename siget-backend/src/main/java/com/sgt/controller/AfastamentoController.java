package com.sgt.controller;

import com.sgt.dto.request.AfastamentoRequest;
import com.sgt.dto.response.AfastamentoResponse;
import com.sgt.service.AfastamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/afastamentos")
@RequiredArgsConstructor
public class AfastamentoController {

    private final AfastamentoService afastamentoService;

    @GetMapping("/funcionario/{funcionarioId}")
    public List<AfastamentoResponse> listarPorFuncionario(@PathVariable Long funcionarioId) {
        return afastamentoService.listarPorFuncionario(funcionarioId);
    }

    @GetMapping("/tipo/{tipo}")
    public List<AfastamentoResponse> listarPorTipo(@PathVariable String tipo) {
        return afastamentoService.listarPorTipo(tipo);
    }

    @PostMapping
    public ResponseEntity<AfastamentoResponse> cadastrar(@RequestBody AfastamentoRequest request) {
        return ResponseEntity.status(201).body(afastamentoService.cadastrar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AfastamentoResponse> atualizar(
            @PathVariable Long id,
            @RequestBody AfastamentoRequest request) {
        return ResponseEntity.ok(afastamentoService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        afastamentoService.desativar(id);
        return ResponseEntity.noContent().build();
    }
}