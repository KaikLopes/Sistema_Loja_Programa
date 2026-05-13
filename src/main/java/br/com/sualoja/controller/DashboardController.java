package br.com.sualoja.controller;

import br.com.sualoja.dao.ProdutoDAO;
import br.com.sualoja.dao.VendaDAO;
import br.com.sualoja.dao.VendaItemDAO;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
public class DashboardController {

    private final VendaDAO vendaDAO;
    private final ProdutoDAO produtoDAO;
    private final VendaItemDAO vendaItemDAO;

    public DashboardController(VendaDAO vendaDAO, ProdutoDAO produtoDAO, VendaItemDAO vendaItemDAO) {
        this.vendaDAO = vendaDAO;
        this.produtoDAO = produtoDAO;
        this.vendaItemDAO = vendaItemDAO;
    }

    /**
     * Calcula a data de início com base no índice do ComboBox da View
     */
    private LocalDateTime calcularDataInicio(int indexFiltro) {
        LocalDateTime agora = LocalDateTime.now();
        switch (indexFiltro) {
            case 0: return agora.with(LocalTime.MIN); // Hoje
            case 1: return agora.minusDays(7).with(LocalTime.MIN); // Esta Semana
            case 2: return agora.withDayOfMonth(1).with(LocalTime.MIN); // Este Mês
            case 3: return agora.withDayOfYear(1).with(LocalTime.MIN); // Este Ano
            default: return null; // Total Geral
        }
    }

    public BigDecimal getFaturamento(int indexFiltro) {
        LocalDateTime inicio = calcularDataInicio(indexFiltro);
        if (inicio == null) {
            BigDecimal total = vendaDAO.calcularTotalFaturamento(); //
            return total != null ? total : BigDecimal.ZERO;
        }
        BigDecimal total = vendaDAO.calcularFaturamentoPorPeriodo(inicio, LocalDateTime.now().with(LocalTime.MAX)); //
        return total != null ? total : BigDecimal.ZERO;
    }

    public long getQtdVendas(int indexFiltro) {
        LocalDateTime inicio = calcularDataInicio(indexFiltro);
        if (inicio == null) return vendaDAO.count();
        return vendaDAO.countByDataHoraBetween(inicio, LocalDateTime.now().with(LocalTime.MAX)); //
    }

    public BigDecimal getValorEstoque() {
        BigDecimal total = produtoDAO.calcularValorTotalEstoque(); //
        return total != null ? total : BigDecimal.ZERO;
    }

    public List<Object[]> getRanking(int indexFiltro) {
        LocalDateTime inicio = calcularDataInicio(indexFiltro);
        if (inicio == null) return vendaItemDAO.buscarRankingMaisVendidos(); //
        return vendaItemDAO.buscarRankingPorPeriodo(inicio, LocalDateTime.now().with(LocalTime.MAX)); //
    }
}
