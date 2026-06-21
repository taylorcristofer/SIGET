package com.sgt.dto.response;

import com.sgt.entity.Dependente;

import java.time.LocalDate;

public record DependenteResponse(
    Long id,
    Long funcionarioId,
    String nomeFuncionario,
    String nome,
    String parentesco,
    LocalDate dataNascimento,
    String cpf,
    Boolean ativo
) {
    public static DependenteResponse from(Dependente d) {
        return new DependenteResponse(
            d.getId(),
            d.getFuncionario().getId(),
            d.getFuncionario().getNome(),
            d.getNome(),
            d.getParentesco(),
            d.getDataNascimento(),
            d.getCpf(),
            d.getAtivo()
        );
    }
}