package com.sgt.dto.request;

import java.time.LocalDate;

public record DocumentoRequest(
    Long funcionarioId,
    String tipo,
    String descricao,
    LocalDate dataEmissao,
    LocalDate dataVencimento,
    Boolean alertar
) {}