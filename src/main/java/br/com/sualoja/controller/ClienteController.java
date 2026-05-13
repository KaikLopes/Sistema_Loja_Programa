package br.com.sualoja.controller;

import br.com.sualoja.model.Cliente;
import br.com.sualoja.service.ClienteService;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    public List<Cliente> buscarTodos() {
        return service.buscarTodos();
    }

    public Cliente buscarPorId(Long id) {
        return service.buscarPorId(id);
    }

    public List<Cliente> buscarPorNome(String nome) {
        return service.buscarPorNome(nome);
    }

    // A mágica: A tela chama só o salvar(). O Controller descobre o que fazer!
    public void salvar(Cliente cliente) {
        if (cliente.getId() != null) {
            service.atualizar(cliente.getId(), cliente);
        } else {
            service.salvar(cliente);
        }
    }

    public void excluir(Long id) {
        service.deletar(id);
    }
}
