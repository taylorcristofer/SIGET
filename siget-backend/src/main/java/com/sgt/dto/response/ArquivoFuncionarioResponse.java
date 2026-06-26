package com.sgt.dto.response;

import com.sgt.entity.ArquivoFuncionario;
import java.time.LocalDateTime;

public record ArquivoFuncionarioResponse(
    Long id,
    Long funcionarioId,
    String nomeFuncionario,
    String categoria,
    String nomeArquivo,
    String s3Key,
    String url,
    String mesReferencia,
    Boolean ativo,
    LocalDateTime criadoEm
) {
    public static ArquivoFuncionarioResponse from(ArquivoFuncionario a) {
        return new ArquivoFuncionarioResponse(
            a.getId(),
            a.getFuncionario().getId(),
            a.getFuncionario().getNome(),
            a.getCategoria(),
            a.getNomeArquivo(),
            a.getS3Key(),
            a.getUrl(),
            a.getMesReferencia(),
            a.getAtivo(),
            a.getCriadoEm()
        );
    }
}