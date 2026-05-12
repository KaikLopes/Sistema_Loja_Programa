package br.com.sualoja.controller;

import br.com.sualoja.dao.ProdutoDAO;
import br.com.sualoja.dao.VendaDAO;
import br.com.sualoja.model.Produto;
import br.com.sualoja.model.Venda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
public class RelatorioController {

    @Autowired private VendaDAO vendaDAO;
    @Autowired private ProdutoDAO produtoDAO;

    public List<Venda> buscarVendasNoPeriodo(int filtroIndex) {
        LocalDateTime[] periodo = calcularDatas(filtroIndex);
        return vendaDAO.findByDataHoraBetween(periodo[0], periodo[1]);
    }

    public List<Produto> buscarProdutosNoPeriodo(int filtroIndex) {
        LocalDateTime[] periodo = calcularDatas(filtroIndex);
        return produtoDAO.findByCriadoEmBetween(periodo[0], periodo[1]);
    }

    public BigDecimal calcularTotalSaidas(List<Produto> produtos) {
        BigDecimal total = BigDecimal.ZERO;
        for (Produto p : produtos) {
            BigDecimal custo = (p.getUltimoPrecoCompra() != null) ? p.getUltimoPrecoCompra() : BigDecimal.ZERO;
            Integer qtd = (p.getQuantidadeInicial() != null) ? p.getQuantidadeInicial() : p.getQuantidadeEstoque();
            if (qtd == null) qtd = 0;
            total = total.add(custo.multiply(new BigDecimal(qtd)));
        }
        return total;
    }

    public BigDecimal calcularTotalEntradas(List<Venda> vendas) {
        BigDecimal total = BigDecimal.ZERO;
        for (Venda v : vendas) {
            if (v.getValorTotal() != null) total = total.add(v.getValorTotal());
        }
        return total;
    }

    private LocalDateTime[] calcularDatas(int index) {
        LocalDateTime agora = LocalDateTime.now().with(LocalTime.MAX);
        LocalDateTime inicio;
        switch (index) {
            case 0: inicio = agora.with(LocalTime.MIN); break; // Hoje
            case 1: inicio = agora.minusDays(7).with(LocalTime.MIN); break; // Semana
            case 2: inicio = agora.withDayOfMonth(1).with(LocalTime.MIN); break; // Mês
            default: inicio = agora.withDayOfYear(1).with(LocalTime.MIN); break; // Ano
        }
        return new LocalDateTime[]{inicio, agora};
    }
}