package br.com.sualoja.dao;

import br.com.sualoja.model.Pessoa;
import jakarta.persistence.EntityManager;
import java.util.List;

public class PessoaDAO {

    private EntityManager em;

    public PessoaDAO(EntityManager em) {
        this.em = em;
    }

    public void cadastrar(Pessoa pessoa) {
        this.em.persist(pessoa);
    }

    public Pessoa buscarPorId(Long id) {
        return this.em.find(Pessoa.class, id);
    }

    public List<Pessoa> buscarTodos() {
        String jpql = "SELECT p FROM Pessoa p";
        return em.createQuery(jpql, Pessoa.class).getResultList();
    }
}