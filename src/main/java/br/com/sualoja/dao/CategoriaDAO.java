package br.com.sualoja.dao;

import br.com.sualoja.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoriaDAO extends JpaRepository<Categoria, Integer> {
    // Para listar em ordem alfabética
    List<Categoria> findAllByOrderByNomeAsc();
}