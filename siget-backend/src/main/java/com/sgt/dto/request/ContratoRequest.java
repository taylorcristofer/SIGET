package com.sgt.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ContratoRequest {
    
    @NotNull(message = "Órgão é obrigatório")
    private Long orgaoId;

    @NotBlank(message = "Número do contrato é obrigatório")
    private String numero;

    @NotNull(message = "Data de início é obrigatória")
    private LocalDate dataInicio;

    private LocalDate dataFim;
    private String objeto;
    private BigDecimal valorMensal;
}
