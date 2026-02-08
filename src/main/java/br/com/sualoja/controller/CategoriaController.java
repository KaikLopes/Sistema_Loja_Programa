package br.com.sualoja.controller;

import br.com.sualoja.dao.CategoriaDAO;
import br.com.sualoja.model.Categoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
public class CategoriaController {

    @Autowired
    private CategoriaDAO categoriaDAO;

    public void salvar(Categoria categoria) {
        categoriaDAO.save(categoria);
    }

    public List<Categoria> listarTodas() {
        return categoriaDAO.findAllByOrderByNomeAsc();
    }

    public void excluir(Integer id) {
        categoriaDAO.deleteById(id);
    }
}