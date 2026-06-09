package com.sgt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contratos")
public class Contrato {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "orgao_id", nullable = false)
    private Orgao orgao;

    @Column(unique = true, nullable = false, length = 50)
    private String numero;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    private LocalDate dataFim;

    @Column(columnDefinition = "TEXT")
    private String objeto;

    @Column(name = "valor_mensal", precision = 12, scale = 2)
    private BigDecimal valorMensal;

    @Column(nullable = false)
    private Boolean ativo;

    @Column(name = "arquivo_pdf")
    private String arquivoPdf;

    @PrePersist
    public void prePersist() {
        if (this.ativo == null) this.ativo = true;
    }
}
