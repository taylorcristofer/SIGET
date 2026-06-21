package com.sgt.repository;

import com.sgt.entity.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface DocumentoRepository extends JpaRepository<Documento, Long> {

    List<Documento> findByFuncionarioIdAndAtivoTrue(Long funcionarioId);

    // Documentos que vencem nos próximos X dias e têm alertar = true
    @Query("""
        SELECT d FROM Documento d
        WHERE d.ativo = true
          AND d.alertar = true
          AND d.dataVencimento IS NOT NULL
          AND d.dataVencimento BETWEEN :hoje AND :limite
        ORDER BY d.dataVencimento ASC
    """)
    List<Documento> findDocumentosVencendo(LocalDate hoje, LocalDate limite);

    // Documentos já vencidos
    @Query("""
        SELECT d FROM Documento d
        WHERE d.ativo = true
          AND d.alertar = true
          AND d.dataVencimento IS NOT NULL
          AND d.dataVencimento < :hoje
        ORDER BY d.dataVencimento ASC
    """)
    List<Documento> findDocumentosVencidos(LocalDate hoje);
}