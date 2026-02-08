package br.com.sualoja.dao;

import br.com.sualoja.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ProdutoDAO extends JpaRepository<Produto, Integer> {
    
    List<Produto> findByAtivoTrueOrderByNomeAsc();

    @Query("SELECT SUM(p.quantidadeEstoque * p.precoVenda) FROM Produto p WHERE p.ativo = true")
    BigDecimal calcularValorTotalEstoque();

    List<Produto> findByFornecedorId(Long id);

    // --- NOVO: Busca produtos por Categoria ---
    List<Produto> findByCategoriaId(Integer id);

    List<Produto> findByCriadoEmBetween(LocalDateTime inicio, LocalDateTime fim);
}