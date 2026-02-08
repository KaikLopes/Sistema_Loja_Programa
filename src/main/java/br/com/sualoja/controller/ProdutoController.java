package br.com.sualoja.controller;

import br.com.sualoja.dao.ProdutoDAO;
import br.com.sualoja.model.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
public class ProdutoController {

    @Autowired
    private ProdutoDAO produtoDAO;

    public void salvar(Produto produto) {
        if (produto.getId() == null) {
            produtoDAO.cadastrar(produto);
        } else {
            produtoDAO.atualizar(produto);
        }
    }

    public List<Produto> listarTodos() {
        return produtoDAO.buscarTodos();
    }

    public Produto buscarPorId(Integer id) {
        return produtoDAO.buscarPorId(id);
    }

    public void excluir(Produto produto) {
        produtoDAO.remover(produto);
    }
}