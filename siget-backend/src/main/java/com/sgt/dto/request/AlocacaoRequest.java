package com.sgt.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AlocacaoRequest {
    
    @NotNull(message = "Funcionário é obrigatório")
    private Long funcionarioId;

    @NotNull(message = "Contrato é obrigatório")
    private Long contratoId;

    @NotBlank(message = "Função alocada é obrigatória")
    private String funcaoAlocada;

    @NotNull(message = "Data de início é obrigatória")
    private LocalDate dataInicio;

    private LocalDate dataFim;
    private String turno;
    private String observacoes;
}
