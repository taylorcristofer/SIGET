package com.sgt.dto.request;

import java.time.LocalDate;

public record DependenteRequest(
    Long funcionarioId,
    String nome,
    String parentesco,
    LocalDate dataNascimento,
    String cpf
) {}