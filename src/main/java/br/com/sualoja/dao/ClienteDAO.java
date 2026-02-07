package br.com.sualoja.dao;

import br.com.sualoja.model.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository // <--- Adicione
public class ClienteDAO {

    @PersistenceContext
    private EntityManager em;

    public void cadastrar(Cliente cliente) {
        this.em.persist(cliente);
    }

    public Cliente buscarPorId(Long id) {
        return this.em.find(Cliente.class, id);
    }
}