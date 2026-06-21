package com.sgt.dto.response;

import com.sgt.entity.Documento;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DocumentoResponse(
    Long id,
    Long funcionarioId,
    String nomeFuncionario,
    String tipo,
    String descricao,
    LocalDate dataEmissao,
    LocalDate dataVencimento,
    Boolean alertar,
    Boolean ativo,
    LocalDateTime criadoEm,
    String statusVencimento  // "OK", "VENCENDO", "VENCIDO"
) {
    public static DocumentoResponse from(Documento d) {
        String status = calcularStatus(d.getDataVencimento());
        return new DocumentoResponse(
            d.getId(),
            d.getFuncionario().getId(),
            d.getFuncionario().getNome(),
            d.getTipo(),
            d.getDescricao(),
            d.getDataEmissao(),
            d.getDataVencimento(),
            d.getAlertar(),
            d.getAtivo(),
            d.getCriadoEm(),
            status
        );
    }

    private static String calcularStatus(LocalDate dataVencimento) {
        if (dataVencimento == null) return "SEM_VENCIMENTO";
        LocalDate hoje = LocalDate.now();
        if (dataVencimento.isBefore(hoje)) return "VENCIDO";
        if (dataVencimento.isBefore(hoje.plusDays(30))) return "VENCENDO";
        return "OK";
    }
}