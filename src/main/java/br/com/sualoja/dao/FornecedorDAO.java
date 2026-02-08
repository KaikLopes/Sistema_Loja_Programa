package br.com.sualoja.dao;

import br.com.sualoja.model.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FornecedorDAO extends JpaRepository<Fornecedor, Long> {
    // Método simplificado que funciona sem erro
    List<Fornecedor> findAllByOrderByNomeFantasiaAsc();
}