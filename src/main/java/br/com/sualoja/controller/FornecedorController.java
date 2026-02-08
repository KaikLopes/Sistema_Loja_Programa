package br.com.sualoja.controller;

import br.com.sualoja.dao.FornecedorDAO;
import br.com.sualoja.model.Fornecedor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class FornecedorController {

    @Autowired
    private FornecedorDAO fornecedorDAO;

    public void salvar(Fornecedor fornecedor) {
        if (fornecedor.getId() == null) {
            fornecedorDAO.cadastrar(fornecedor);
        } else {
            fornecedorDAO.atualizar(fornecedor);
        }
    }

    public Fornecedor buscarPorId(Integer id) {
        return fornecedorDAO.buscarPorId(id);
    }

    public void excluir(Fornecedor fornecedor) {
        fornecedorDAO.remover(fornecedor);
    }
}