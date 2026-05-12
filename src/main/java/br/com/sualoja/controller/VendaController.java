package br.com.sualoja.controller;

import br.com.sualoja.dao.ClienteDAO;
import br.com.sualoja.dao.ProdutoDAO;
import br.com.sualoja.dao.VendaDAO;
import br.com.sualoja.dao.VendaItemDAO;
import br.com.sualoja.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class VendaController {

    @Autowired private VendaDAO vendaDAO;
    @Autowired private ProdutoDAO produtoDAO;
    @Autowired private ClienteDAO clienteDAO;
    @Autowired private VendaItemDAO vendaItemDAO;

    @Transactional
    public void realizarVenda(Venda venda, List<VendaItem> itens) throws Exception {
        if (itens.isEmpty()) throw new Exception("O carrinho está vazio!");
        if (venda.getCliente() == null) throw new Exception("Selecione um cliente!");

        venda.setDataHora(LocalDateTime.now());
        vendaDAO.save(venda);

        for (VendaItem item : itens) {
            Produto p = item.getProduto();
            if (p.getQuantidadeEstoque() < item.getQuantidade()) {
                throw new Exception("Estoque insuficiente para: " + p.getNome());
            }

            p.setQuantidadeEstoque(p.getQuantidadeEstoque() - item.getQuantidade());
            produtoDAO.save(p);

            item.setVenda(venda);
        }

        venda.setItens(itens);
        vendaDAO.save(venda);
    }

    public List<Venda> listarVendas(String busca, boolean ordenarPorValor) {
        if (busca != null && !busca.isEmpty()) {
            return vendaDAO.findByClienteNomeContainingIgnoreCase(busca);
        }

        Sort sort = ordenarPorValor ?
                Sort.by(Sort.Direction.DESC, "valorTotal") :
                Sort.by(Sort.Direction.DESC, "dataHora");

        return vendaDAO.findAll(sort);
    }

    // =========================================================
    // NOVOS MÉTODOS PARA ABASTECER AS TELAS DE VENDA (SEM DAOS)
    // =========================================================

    public List<Cliente> buscarClientesParaVenda() {
        return clienteDAO.findAllByOrderByNomeAsc();
    }

    public List<Produto> buscarProdutosAtivos() {
        return produtoDAO.findByAtivoTrueOrderByNomeAsc();
    }

    public List<VendaItem> buscarItensDaVenda(Long idVenda) {
        return vendaItemDAO.findByVendaId(idVenda);
    }

    @Transactional
    public void cadastrarClienteRapido(String nome) {
        Cliente c = new Cliente();
        c.setNome(nome);
        c.setCpf("00000000000"); // CPF genérico para cadastro rápido
        c.setEndereco("-");
        c.setTelefone("-");
        clienteDAO.save(c);
    }
}