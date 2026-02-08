package br.com.sualoja.dao;

import br.com.sualoja.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param; // <--- IMPORTANTE
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface VendaDAO extends JpaRepository<Venda, Long> {
    
    List<Venda> findByClienteNomeContainingIgnoreCase(String nome);

    @Query("SELECT COALESCE(SUM(v.valorTotal), 0) FROM Venda v")
    BigDecimal calcularTotalFaturamento();

    List<Venda> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);

    // --- CORREÇÃO: Adicionado @Param para garantir o funcionamento ---
    @Query("SELECT COALESCE(SUM(v.valorTotal), 0) FROM Venda v WHERE v.dataHora BETWEEN :inicio AND :fim")
    BigDecimal calcularFaturamentoPorPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    long countByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);
}