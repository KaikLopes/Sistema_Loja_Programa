package br.com.sualoja.controller;

import br.com.sualoja.dao.UsuarioDAO;
import br.com.sualoja.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioDAO usuarioDAO;

    // Lógica de Login
    public Usuario autenticar(String login, String senha) throws Exception {
        Usuario usuario = usuarioDAO.findByLogin(login);
        
        if (usuario == null) {
            throw new Exception("Usuário não encontrado!");
        }
        
        if (!usuario.getSenha().equals(senha)) {
            throw new Exception("Senha incorreta!");
        }
        
        return usuario;
    }

    // Lógica de Cadastro
    public void cadastrar(String nome, String login, String senha, String confSenha) throws Exception {
        // 1. Validações básicas
        if (nome.isEmpty() || login.isEmpty() || senha.isEmpty()) {
            throw new Exception("Preencha todos os campos obrigatórios!");
        }
        
        if (!senha.equals(confSenha)) {
            throw new Exception("As senhas não conferem!");
        }

        // 2. Verifica se já existe
        if (usuarioDAO.findByLogin(login) != null) {
            throw new Exception("Este login já está em uso!");
        }

        // 3. Cria o objeto
        // OBS: Como sua tela de cadastro ainda não pede CPF, vou passar null ou vazio por enquanto.
        // O construtor é: (nome, cpf, login, senha)
        Usuario novoUsuario = new Usuario(nome, "000.000.000-00", login, senha);
        
        // 4. Salva no banco
        usuarioDAO.save(novoUsuario);
    }
}