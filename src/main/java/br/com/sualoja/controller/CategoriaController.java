package br.com.sualoja.controller;

import br.com.sualoja.dao.CategoriaDAO;
import br.com.sualoja.model.Categoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CategoriaController {

    @Autowired
    private CategoriaDAO categoriaDAO;

    public void salvar(Categoria categoria) {
        categoriaDAO.cadastrar(categoria);
    }

    public Categoria buscarPorId(Integer id) {
        return categoriaDAO.buscarPorId(id);
    }

    public void excluir(Categoria categoria) {
        categoriaDAO.remover(categoria);
    }
}