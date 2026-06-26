package com.sgt.controller;

import com.sgt.dto.request.ArquivoFuncionarioRequest;
import com.sgt.dto.response.ArquivoFuncionarioResponse;
import com.sgt.service.ArquivoFuncionarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/arquivos")
@RequiredArgsConstructor
public class ArquivoFuncionarioController {

    private final ArquivoFuncionarioService arquivoService;

    @GetMapping("/funcionario/{funcionarioId}")
    public List<ArquivoFuncionarioResponse> listarPorFuncionario(
            @PathVariable Long funcionarioId) {
        return arquivoService.listarPorFuncionario(funcionarioId);
    }

    @GetMapping("/funcionario/{funcionarioId}/categoria/{categoria}")
    public List<ArquivoFuncionarioResponse> listarPorCategoria(
            @PathVariable Long funcionarioId,
            @PathVariable String categoria) {
        return arquivoService.listarPorCategoria(funcionarioId, categoria);
    }

    @PostMapping
    public ResponseEntity<ArquivoFuncionarioResponse> cadastrar(
            @RequestBody ArquivoFuncionarioRequest request) {
        return ResponseEntity.status(201)
            .body(arquivoService.cadastrar(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        arquivoService.desativar(id);
        return ResponseEntity.noContent().build();
    }
}