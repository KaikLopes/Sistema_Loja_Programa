package br.com.sualoja.dao;

import br.com.sualoja.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioDAO extends JpaRepository<Usuario, Long> {

    // A mágica acontece aqui:
    // Só de declarar essa linha, o Spring cria o SQL "SELECT * FROM ... WHERE login = ?"
    Usuario findByLogin(String login);

}