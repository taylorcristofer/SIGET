package com.sgt.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class ContratoResponse {
    
    private Long id;
    private Long orgaoId;
    private String orgaoNome;
    private String orgaoSigla;
    private String numero;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String objeto;
    private BigDecimal valorMensal;
    private Boolean ativo;
}
