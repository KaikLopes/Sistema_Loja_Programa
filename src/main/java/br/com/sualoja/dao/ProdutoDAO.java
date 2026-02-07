package br.com.sualoja.dao;

import br.com.sualoja.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// IMPORTANTE:
// 1. Tem que ser 'interface' e não 'class'
// 2. Tem que estender JpaRepository<Entidade, TipoDoId>
// No seu caso, o ID do Produto é Integer
@Repository
public interface ProdutoDAO extends JpaRepository<Produto, Integer> {

    // Não precisa escrever nada aqui dentro!
    // O Spring cria o findAll, save, deleteById automaticamente pra você.

}