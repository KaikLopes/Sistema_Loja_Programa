package br.com.sualoja.dao;

import br.com.sualoja.model.Categoria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class CategoriaDAO {

    @PersistenceContext
    private EntityManager em;

    public CategoriaDAO(EntityManager em) {
        this.em = em;
    }

    public void cadastrar(Categoria categoria) {
        this.em.persist(categoria);
    }

    public Categoria buscarPorId(Integer id) {
        return this.em.find(Categoria.class, id);
    }

    public void remover(Categoria categoria) {
        categoria = this.em.merge(categoria);
        this.em.remove(categoria);
    }
}