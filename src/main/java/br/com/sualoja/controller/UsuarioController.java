package br.com.sualoja.controller;

import br.com.sualoja.dao.UsuarioDAO;
import br.com.sualoja.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioDAO usuarioDAO;

    public void salvar(Usuario usuario) {
        usuarioDAO.cadastrar(usuario);
    }

    public Usuario buscarPorId(Long id) {
        return usuarioDAO.buscarPorId(id);
    }
}