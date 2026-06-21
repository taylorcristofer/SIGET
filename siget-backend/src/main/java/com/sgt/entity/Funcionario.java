package com.sgt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "funcionarios")
public class Funcionario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(length = 20)
    private String rg;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(length = 20)
    private String telefone;

    @Column(length = 100)
    private String email;

    @Column(nullable = false, length = 50)
    private String funcao;

    @Column(length = 20)
    private String status;

    @Column(nullable = false)
    private Boolean ativo;

    @Column(name = "data_admissao")
    private LocalDate dataAdmissao;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @Column(name = "num_camisa", length = 10)
    private String camisa;

    @Column(name = "num_calca", length = 10)
    private String calca;

    @Column(name = "num_bota", length = 10)
    private String bota;
    
    @PrePersist
    public void PrePersist(){
        this.criadoEm = LocalDateTime.now();
        if (this.ativo == null) this.ativo = true;
        if (this.status == null) this.status = "ATIVO";
    }
}