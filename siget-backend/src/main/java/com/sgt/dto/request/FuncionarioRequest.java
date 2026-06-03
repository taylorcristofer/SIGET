package com.sgt.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FuncionarioRequest {
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100)
    private String nome;

    @NotBlank(message = "CPF é obrigatório")
    @Size(min = 11, max = 14)
    private String cpf;

    private String rg;
    private LocalDate dataNascimento;
    private String telefone;
    private String email;

    private String camisa;
    private String calca;
    private String bota;

    @NotBlank(message = "Função é obrigatória")
    private String funcao;

    private LocalDate dataAdmissao;
}
