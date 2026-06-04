package com.sgt.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrgaoResponse {
    
    private Long id;
    private String nome;
    private String sigla;
    private String cnpj;
    private String endereco;
    private String cidade;
    private String contatoNome;
    private String contatoTel;
    private Boolean ativo;
}
