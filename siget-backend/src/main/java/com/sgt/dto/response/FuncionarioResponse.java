package com.sgt.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class FuncionarioResponse {
    
    private Long id;
    private String nome;
    private String cpf;
    private String rg;
    private LocalDate dataNascimento;
    private String telefone;
    private String email;
    private String funcao;
    private String status;
    private Boolean ativo;
    private LocalDate dataAdmissao;
    private LocalDateTime criadoEm;
}
