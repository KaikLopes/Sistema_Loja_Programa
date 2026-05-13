package br.com.sualoja.controller;

import br.com.sualoja.model.Fornecedor;
import br.com.sualoja.service.FornecedorService;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class FornecedorController {

    private final FornecedorService service;

    public FornecedorController(FornecedorService service) {
        this.service = service;
    }

    public List<Fornecedor> buscarTodos() {
        return service.buscarTodos();
    }

    public void salvar(Fornecedor fornecedor) {
        if (fornecedor.getId() != null) {
            service.atualizar(fornecedor.getId(), fornecedor);
        } else {
            service.salvar(fornecedor);
        }
    }

    public void excluir(Long id) {
        service.deletar(id);
    }
}
