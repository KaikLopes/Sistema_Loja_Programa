package br.com.sualoja.controller;

import br.com.sualoja.dao.VendaDAO;
import br.com.sualoja.model.Venda;
import br.com.sualoja.model.VendaItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
public class VendaController {

    @Autowired
    private VendaDAO vendaDAO;

    public void finalizarVenda(Venda venda) {
        if (venda.getItens().isEmpty()) {
            throw new RuntimeException("Não é possível realizar uma venda sem itens!");
        }
        vendaDAO.cadastrar(venda);
    }

    public List<Venda> listarTodas() {
        return vendaDAO.buscarTodos();
    }

    public Venda buscarPorId(Long id) {
        return vendaDAO.buscarPorId(id);
    }
}