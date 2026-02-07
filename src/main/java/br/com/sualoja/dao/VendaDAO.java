package br.com.sualoja.dao;

import br.com.sualoja.model.Venda;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository; // <--- Importante
import org.springframework.transaction.annotation.Transactional; // <--- Importante
import java.util.List;

@Repository // <--- Isso diz pro Spring: "Eu cuido do banco, me use!"
public class VendaDAO {

    @PersistenceContext // <--- O Spring injeta o EntityManager aqui sozinho
    private EntityManager em;

    // Pode remover o construtor manual se quiser, o @PersistenceContext resolve
    
    @Transactional // <--- Garante que salva tudo ou nada (commit/rollback)
    public void cadastrar(Venda venda) {
        this.em.persist(venda);
    }

    public Venda buscarPorId(Long id) {
        return this.em.find(Venda.class, id);
    }

    public List<Venda> buscarTodos() {
        String jpql = "SELECT v FROM Venda v";
        return em.createQuery(jpql, Venda.class).getResultList();
    }
}