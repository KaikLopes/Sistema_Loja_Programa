package br.com.sualoja.controller;

import br.com.sualoja.dao.ClienteDAO;
import br.com.sualoja.model.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ClienteController {

    @Autowired
    private ClienteDAO clienteDAO;

    public void salvar(Cliente cliente) {
        clienteDAO.cadastrar(cliente);
    }

    public Cliente buscarPorId(Long id) {
        return clienteDAO.buscarPorId(id);
    }
}