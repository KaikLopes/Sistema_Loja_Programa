package br.com.sualoja.dao;

import br.com.sualoja.model.Fornecedor;
import jakarta.persistence.EntityManager;

public class FornecedorDAO {

    private EntityManager em;

    public FornecedorDAO(EntityManager em) {
        this.em = em;
    }

    public void cadastrar(Fornecedor fornecedor) {
        this.em.persist(fornecedor);
    }

    public Fornecedor buscarPorId(Integer id) {
        return this.em.find(Fornecedor.class, id);
    }

    public void atualizar(Fornecedor fornecedor) {
        this.em.merge(fornecedor);
    }

    public void remover(Fornecedor fornecedor) {
        fornecedor = this.em.merge(fornecedor);
        this.em.remove(fornecedor);
    }
}