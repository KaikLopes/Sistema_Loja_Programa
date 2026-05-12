package br.com.sualoja.controller;
import br.com.sualoja.model.Categoria;
import br.com.sualoja.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class CategoriaController{

    @Autowired
    private CategoriaService categoriaService;

    public void salvar(Categoria categoria){
        if(categoria.getId() != null){
            categoriaService.atualizar(categoria.getId(), categoria);
        }
        else{
            categoriaService.salvar(categoria);
        }
    }

    public List<Categoria> listarTodas(){
        return categoriaService.listarTodos();
    }

    public void excluir(Integer id){
        categoriaService.deletar(id);
    }
}