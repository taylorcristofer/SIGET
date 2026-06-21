package com.sgt.dto.request;

import java.time.LocalDate;

public record FeriasRequest(
    Long funcionarioId,
    LocalDate dataInicio,
    LocalDate dataFim,
    Integer diasDireito,
    Integer diasGozados
) {}