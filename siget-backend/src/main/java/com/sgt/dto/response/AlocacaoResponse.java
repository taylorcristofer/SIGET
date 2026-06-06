package com.sgt.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class AlocacaoResponse {
    
     private Long id;

    private Long funcionarioId;
    private String funcionarioNome;
    private String funcionarioCpf;
    private String funcionarioFuncao;

    private Long contratoId;
    private String contratoNumero;
    private Long orgaoId;
    private String orgaoNome;
    private String orgaoSigla;

    private String funcaoAlocada;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String turno;
    private String status;
    private String observacoes;
    private LocalDateTime criadoEm;
}
