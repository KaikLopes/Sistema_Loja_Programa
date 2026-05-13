package br.com.sualoja.service;

import br.com.sualoja.dao.ClienteDAO;
import br.com.sualoja.model.Cliente;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteDAO clienteDAO;

    public ClienteService(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    public List<Cliente> buscarTodos() {
        return clienteDAO.findAllByOrderByNomeAsc();
    }

    public Cliente buscarPorId(Long id) {
        return clienteDAO.findById(id).orElse(null);
    }

    public List<Cliente> buscarPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return buscarTodos();
        }
        return clienteDAO.findAllByOrderByNomeAsc().stream()
                .filter(c -> c.getNome().toLowerCase().contains(nome.toLowerCase()))
                .toList();
    }

    @Transactional
    public void salvar(Cliente cliente) {
        validarRegras(cliente);
        clienteDAO.save(cliente);
    }

    @Transactional
    public void atualizar(Long id, Cliente clienteAtualizado) {
        Cliente clienteExistente = clienteDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado!"));

        validarRegras(clienteAtualizado);

        // Mapeamento completo
        clienteExistente.setNome(clienteAtualizado.getNome());
        clienteExistente.setCpf(clienteAtualizado.getCpf());
        clienteExistente.setTelefone(clienteAtualizado.getTelefone());
        clienteExistente.setEndereco(clienteAtualizado.getEndereco());

        // ATENÇÃO: Se você adicionou 'email' na classe Cliente.java, descomente a linha abaixo:
        // clienteExistente.setEmail(clienteAtualizado.getEmail());

        clienteDAO.save(clienteExistente);
    }

    @Transactional
    public void deletar(Long id) {
        if (!clienteDAO.existsById(id)) {
            throw new RuntimeException("Cliente não encontrado para exclusão.");
        }
        clienteDAO.deleteById(id);
    }

    // --- REGRAS DE NEGÓCIO BLINDADAS ---
    private void validarRegras(Cliente c) {
        if (c.getNome() == null || c.getNome().trim().isEmpty()) {
            throw new RuntimeException("O nome é obrigatório!");
        }

        // Aqui você pode adicionar as validações de CPF e Telefone depois,
        // para garantir que não entram dados sujos no banco!
    }
}
