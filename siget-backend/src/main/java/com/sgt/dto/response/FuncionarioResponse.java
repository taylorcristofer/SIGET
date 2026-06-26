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
    private String rgOrgaoEmissor;
    private LocalDate rgDataEmissao;
    private LocalDate rgDataVencimento;
    private String pisNumero;
    private String ctpsNumero;
    private String ctpsSerie;
    private LocalDate dataNascimento;
    private String telefone;
    private String email;
    private String funcao;
    private String status;
    private Boolean ativo;
    private LocalDate dataAdmissao;
    private LocalDateTime criadoEm;
    private String camisa;
    private String calca;
    private String bota;
}
