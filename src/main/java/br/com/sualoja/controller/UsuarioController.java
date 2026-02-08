package br.com.sualoja.controller;

import br.com.sualoja.dao.UsuarioDAO;
import br.com.sualoja.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioDAO usuarioDAO;

    // Lógica de Login (MANTIDA)
    public Usuario autenticar(String login, String senha) throws Exception {
        Usuario usuario = usuarioDAO.findByLogin(login);
        if (usuario == null) throw new Exception("Usuário não encontrado!");
        if (!usuario.getSenha().equals(senha)) throw new Exception("Senha incorreta!");
        return usuario;
    }

    // Lógica de Cadastro (MANTIDA)
    public void cadastrar(String nome, String login, String senha, String confSenha) throws Exception {
        if (nome.isEmpty() || login.isEmpty() || senha.isEmpty()) throw new Exception("Preencha todos os campos obrigatórios!");
        if (!senha.equals(confSenha)) throw new Exception("As senhas não conferem!");
        if (usuarioDAO.findByLogin(login) != null) throw new Exception("Este login já está em uso!");
        Usuario novoUsuario = new Usuario(nome, "000.000.000-00", login, senha);
        usuarioDAO.save(novoUsuario);
    }

    // --- NOVO MÉTODO PARA ATUALIZAR DADOS ---
    public void atualizarDados(Usuario usuario, String novoNome, String novoLogin, String novaSenha) throws Exception {
        if(novoNome.isEmpty() || novoLogin.isEmpty()) {
            throw new Exception("Nome e Login são obrigatórios.");
        }

        // Se mudou o login, verificar se já existe outro usuário com esse login
        if(!usuario.getLogin().equals(novoLogin)) {
            Usuario existente = usuarioDAO.findByLogin(novoLogin);
            if(existente != null && !existente.getId().equals(usuario.getId())) {
                throw new Exception("Este login já está em uso por outra pessoa.");
            }
        }

        usuario.setNome(novoNome);
        usuario.setLogin(novoLogin);

        // Só atualiza senha se o usuário digitou algo
        if(novaSenha != null && !novaSenha.isEmpty()) {
            usuario.setSenha(novaSenha);
        }

        usuarioDAO.save(usuario);
    }
}