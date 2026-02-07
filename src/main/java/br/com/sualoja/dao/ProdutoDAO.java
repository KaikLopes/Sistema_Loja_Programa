package br.com.sualoja.dao;

import br.com.sualoja.model.Produto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public class ProdutoDAO {

    @PersistenceContext
    private EntityManager em;

    public ProdutoDAO() {
    }

    public ProdutoDAO(EntityManager em) {
        this.em = em;
    }

    @Transactional
    public void cadastrar(Produto produto) {
        this.em.persist(produto);
    }

    public Produto buscarPorId(Integer id) {
        return this.em.find(Produto.class, id);
    }

    @Transactional
    public void atualizar(Produto produto) {
        this.em.merge(produto);
    }

    @Transactional
    public void remover(Produto produto) {
        produto = this.em.merge(produto);
        this.em.remove(produto);
    }

    public List<Produto> buscarTodos() {
        String jpql = "SELECT p FROM Produto p";
        return em.createQuery(jpql, Produto.class).getResultList();
    }
}