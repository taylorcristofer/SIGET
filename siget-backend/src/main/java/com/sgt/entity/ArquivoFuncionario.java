package com.sgt.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "arquivos_funcionario")
public class ArquivoFuncionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcionario_id", nullable = false)
    private Funcionario funcionario;

    // DOCUMENTO_PESSOAL, FOLHA_PONTO, ATESTADO, FERIAS, CONTRACHEQUE
    @Column(nullable = false, length = 30)
    private String categoria;

    @Column(name = "nome_arquivo", nullable = false, length = 255)
    private String nomeArquivo;

    @Column(name = "s3_key", length = 500)
    private String s3Key;

    @Column(length = 1000)
    private String url;

    // Para folhas de ponto e contracheques: "2026-06"
    @Column(name = "mes_referencia", length = 7)
    private String mesReferencia;

    @Column(nullable = false)
    private Boolean ativo;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
        if (this.ativo == null) this.ativo = true;
    }
}