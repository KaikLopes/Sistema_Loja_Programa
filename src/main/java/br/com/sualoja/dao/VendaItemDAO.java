package br.com.sualoja.dao;

import br.com.sualoja.model.VendaItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param; // <--- IMPORTANTE
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VendaItemDAO extends JpaRepository<VendaItem, Long> {

    @Query("SELECT i.produto.nome, SUM(i.quantidade) FROM VendaItem i " +
           "GROUP BY i.produto.nome " +
           "ORDER BY SUM(i.quantidade) DESC " +
           "LIMIT 3")
    List<Object[]> buscarRankingMaisVendidos();
    
    // --- CORREÇÃO: Adicionado @Param ---
    @Query("SELECT i.produto.nome, SUM(i.quantidade) FROM VendaItem i " +
           "WHERE i.venda.dataHora BETWEEN :inicio AND :fim " +
           "GROUP BY i.produto.nome " +
           "ORDER BY SUM(i.quantidade) DESC " +
           "LIMIT 3")
    List<Object[]> buscarRankingPorPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);
    
    List<VendaItem> findByVendaId(Long vendaId);
}