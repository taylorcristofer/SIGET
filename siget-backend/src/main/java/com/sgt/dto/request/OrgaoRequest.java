package com.sgt.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class OrgaoRequest {
    
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 150)
    private String nome;

    @Size(max = 20)
    private String sigla;

    @Size(max = 18)
    private String cnpj;

    @Size(max = 200)
    private String endereco;

    @Size(max = 100)
    private String cidade;

    private String contatoNome;
    private String contatoTel;
}
