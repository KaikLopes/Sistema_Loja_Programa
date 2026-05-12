package br.com.sualoja.service;

import br.com.sualoja.dao.ProdutoDAO;
import br.com.sualoja.dao.VendaDAO;
import br.com.sualoja.dao.VendaItemDAO;
import br.com.sualoja.model.Produto;
import br.com.sualoja.model.Venda;
import br.com.sualoja.model.VendaItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VendaService {

    @Autowired
    private VendaDAO vendaDAO;

    @Autowired
    private VendaItemDAO vendaItemDAO;

    @Autowired
    private ProdutoDAO produtoDAO;

    public List<Venda> listarTodos() {
        return vendaDAO.findAll(Sort.by(Sort.Direction.DESC, "dataHora"));
    }

    public List<Venda> listarPorCliente(String nomeCliente) {
        if (nomeCliente == null || nomeCliente.isEmpty()) {
            return listarTodos();
        }
        return vendaDAO.findByClienteNomeContainingIgnoreCase(nomeCliente);
    }

    public List<Venda> listarOrdenadoPorValor() {
        return vendaDAO.findAll(Sort.by(Sort.Direction.DESC, "valorTotal"));
    }

    public Venda buscarPorId(Long id) {
        return vendaDAO.findById(id).orElse(null);
    }

    public List<VendaItem> buscarItensDaVenda(Long vendaId) {
        return vendaItemDAO.findByVendaId(vendaId);
    }

    @Transactional
    public void salvar(Venda venda) {
        vendaDAO.save(venda);
    }

    @Transactional
    public void realizarVenda(Venda venda, List<VendaItem> itens) {
        if (itens == null || itens.isEmpty()) {
            throw new RuntimeException("A lista de itens está vazia!");
        }
        if (venda.getCliente() == null) {
            throw new RuntimeException("Selecione um cliente para a venda!");
        }

        BigDecimal total = BigDecimal.ZERO;

        // Processa cada item
        for (VendaItem item : itens) {
            Produto produto = item.getProduto();

            // Verifica estoque
            if (produto.getQuantidadeEstoque() < item.getQuantidade()) {
                throw new RuntimeException("Estoque insuficiente para: " + produto.getNome());
            }

            // Baixa estoque
            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - item.getQuantidade());
            produtoDAO.save(produto);

            // Calcula subtotal
            BigDecimal subtotal = produto.getPrecoVenda().multiply(BigDecimal.valueOf(item.getQuantidade()));
            item.setPrecoUnitario(produto.getPrecoVenda());
            item.setSubtotal(subtotal);

            total = total.add(subtotal);
        }

        // Define dados da venda
        venda.setDataHora(LocalDateTime.now());
        venda.setValorTotal(total);
        venda.setItens(itens);

        // Salva a venda
        vendaDAO.save(venda);

        // Vincula itens à venda (cascade deve resolver, mas garantimos aqui)
        for (VendaItem item : itens) {
            item.setVenda(venda);
        }
    }

    @Transactional
    public BigDecimal calcularFaturamentoTotal() {
        return vendaDAO.calcularFaturamentoTotal();
    }

    @Transactional
    public Long contarVendas() {
        return vendaDAO.count();
    }

    @Transactional
    public BigDecimal calcularFaturamentoPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        List<Venda> vendas = vendaDAO.findByDataHoraBetween(inicio, fim);
        return vendas.stream()
                .map(Venda::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional
    public Long contarVendasPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return (long) vendaDAO.findByDataHoraBetween(inicio, fim).size();
    }
}