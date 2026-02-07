package br.com.sualoja.dao;

import br.com.sualoja.model.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository // <--- Adicione
public class UsuarioDAO {

    @PersistenceContext
    private EntityManager em;

    public void cadastrar(Usuario usuario) {
        this.em.persist(usuario);
    }

    public Usuario buscarPorId(Long id) {
        return this.em.find(Usuario.class, id);
    }
}