package br.com.sualoja.dao;

import br.com.sualoja.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClienteDAO extends JpaRepository<Cliente, Long> {
    // Método simplificado que funciona sem erro
    List<Cliente> findAllByOrderByNomeAsc();
}