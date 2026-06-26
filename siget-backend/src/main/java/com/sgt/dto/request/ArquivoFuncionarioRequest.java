package com.sgt.dto.request;


public record ArquivoFuncionarioRequest(
    Long funcionarioId,
    String categoria,
    String nomeArquivo,
    String mesReferencia
) {}