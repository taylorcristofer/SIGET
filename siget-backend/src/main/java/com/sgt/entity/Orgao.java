package com.sgt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orgaos")
public class Orgao {
    
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(length = 20)
    private String sigla;

    @Column(unique = true, length = 18)
    private String cnpj;

    @Column(length = 200)
    private String endereco;

    @Column(length = 100)
    private String cidade;

    @Column(name = "contato_nome", length = 100)
    private String contatoNome;

    @Column(name = "contato_tel", length = 20)
    private String contatoTel;

    @Column(nullable = false)
    private Boolean ativo;

    @PrePersist
    public void prePersist() {
        if (this.ativo == null) this.ativo = true;
    }
}
