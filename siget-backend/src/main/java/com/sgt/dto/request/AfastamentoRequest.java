package com.sgt.dto.request;

import java.time.LocalDate;

public record AfastamentoRequest(
    Long funcionarioId,
    String tipo,
    LocalDate dataInicio,
    LocalDate dataFim,
    Integer dias,
    String observacao
) {}