package com.sgt.dto.response;

import com.sgt.entity.Ferias;

import java.time.LocalDate;

public record FeriasResponse(
    Long id,
    Long funcionarioId,
    String nomeFuncionario,
    LocalDate dataInicio,
    LocalDate dataFim,
    Integer diasDireito,
    Integer diasGozados,
    Integer diasRestantes,
    Boolean ativo
) {
    public static FeriasResponse from(Ferias f) {
        return new FeriasResponse(
            f.getId(),
            f.getFuncionario().getId(),
            f.getFuncionario().getNome(),
            f.getDataInicio(),
            f.getDataFim(),
            f.getDiasDireito(),
            f.getDiasGozados(),
            f.getDiasDireito() - f.getDiasGozados(),
            f.getAtivo()
        );
    }
}