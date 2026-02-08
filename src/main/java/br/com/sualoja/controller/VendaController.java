package br.com.sualoja.controller;

import br.com.sualoja.dao.ProdutoDAO;
import br.com.sualoja.dao.VendaDAO;
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

    // Salvar Venda e Baixar Estoque
    @Transactional
    public void realizarVenda(Venda venda, List<VendaItem> itens) throws Exception {
        if (itens.isEmpty()) throw new Exception("O carrinho está vazio!");
        if (venda.getCliente() == null) throw new Exception("Selecione um cliente!");

        // 1. Salva a Venda primeiro para ter o ID
        venda.setDataHora(LocalDateTime.now());
        vendaDAO.save(venda);

        // 2. Processa cada item
        for (VendaItem item : itens) {
            Produto p = item.getProduto();
            
            // Verifica estoque
            if (p.getQuantidadeEstoque() < item.getQuantidade()) {
                throw new Exception("Estoque insuficiente para: " + p.getNome());
            }

            // Baixa estoque
            p.setQuantidadeEstoque(p.getQuantidadeEstoque() - item.getQuantidade());
            produtoDAO.save(p);

            // Vincula item à venda e salva
            item.setVenda(venda);
            // vendaItemDAO.save(item); // O CascadeType.ALL na Venda resolve isso se configurado, senão salve manual
        }
        
        // Atualiza a lista de itens na venda e salva final (para atualizar valor total se precisar)
        venda.setItens(itens);
        vendaDAO.save(venda);
    }

    public List<Venda> listarVendas(String busca, boolean ordenarPorValor) {
        if (busca != null && !busca.isEmpty()) {
            return vendaDAO.findByClienteNomeContainingIgnoreCase(busca);
        }
        
        // Ordenação
        Sort sort = ordenarPorValor ? 
            Sort.by(Sort.Direction.DESC, "valorTotal") : 
            Sort.by(Sort.Direction.DESC, "dataHora");
            
        return vendaDAO.findAll(sort);
    }
}