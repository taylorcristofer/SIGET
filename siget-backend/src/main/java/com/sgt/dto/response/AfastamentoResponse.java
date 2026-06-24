package com.sgt.dto.response;

import com.sgt.entity.Afastamento;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record AfastamentoResponse(
    Long id,
    Long funcionarioId,
    String nomeFuncionario,
    String tipo,
    LocalDate dataInicio,
    LocalDate dataFim,
    Integer dias,
    String observacao,
    Boolean ativo,
    LocalDateTime criadoEm
) {
    public static AfastamentoResponse from(Afastamento a) {
        return new AfastamentoResponse(
            a.getId(),
            a.getFuncionario().getId(),
            a.getFuncionario().getNome(),
            a.getTipo(),
            a.getDataInicio(),
            a.getDataFim(),
            a.getDias(),
            a.getObservacao(),
            a.getAtivo(),
            a.getCriadoEm()
        );
    }
}