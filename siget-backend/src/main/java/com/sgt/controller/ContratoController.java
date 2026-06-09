package com.sgt.controller;

import com.sgt.dto.request.ContratoRequest;
import com.sgt.dto.response.ContratoResponse;
import com.sgt.service.ContratoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.sgt.entity.Contrato;
import com.sgt.repository.ContratoRepository;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/contratos")
@RequiredArgsConstructor
public class ContratoController {
    
     private final ContratoService service;
     private final ContratoRepository contratoRepository;

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

    @PostMapping("/{id}/upload")
public ResponseEntity<Void> uploadPdf(
        @PathVariable Long id,
        @RequestParam("arquivo") MultipartFile arquivo) {
    try {
        String pasta = "uploads/contratos/";
        new java.io.File(pasta).mkdirs();
        String nomeArquivo = "contrato_" + id + "_" + arquivo.getOriginalFilename();
        String caminho = pasta + nomeArquivo;
        arquivo.transferTo(new java.io.File(caminho));

        Contrato c = contratoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contrato não encontrado"));
        c.setArquivoPdf(caminho);
        contratoRepository.save(c);

        return ResponseEntity.ok().build();
    } catch (Exception e) {
        return ResponseEntity.status(500).build();
    }
}

@GetMapping("/{id}/pdf")
public ResponseEntity<byte[]> verPdf(@PathVariable Long id) {
    try {
        Contrato c = contratoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contrato não encontrado"));

        if (c.getArquivoPdf() == null) {
            return ResponseEntity.notFound().build();
        }

        java.io.File arquivo = new java.io.File(c.getArquivoPdf());
        byte[] bytes = java.nio.file.Files.readAllBytes(arquivo.toPath());

        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "inline; filename=\"contrato.pdf\"")
                .body(bytes);
    } catch (Exception e) {
        return ResponseEntity.status(500).build();
    }
}
}
