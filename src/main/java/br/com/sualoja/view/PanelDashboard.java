package br.com.sualoja.view;

import br.com.sualoja.dao.ProdutoDAO;
import br.com.sualoja.dao.VendaDAO;
import br.com.sualoja.dao.VendaItemDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;

public class PanelDashboard extends JPanel {

    private ProdutoDAO produtoDAO;
    private VendaItemDAO vendaItemDAO;
    private VendaDAO vendaDAO;

    private JLabel lblValorEstoque;
    private JLabel lblFaturamento;
    private JLabel lblVendas;
    
    // Labels de Título (Para mudar dinamicamente)
    private JLabel lblTituloFat;
    private JLabel lblTituloVendas;
    private JLabel lblTituloRank;
    
    private DefaultTableModel modeloRanking;
    private JComboBox<String> cbFiltro;

    public PanelDashboard(ProdutoDAO pDao, VendaItemDAO viDao, VendaDAO vDao) {
        this.produtoDAO = pDao;
        this.vendaItemDAO = viDao;
        this.vendaDAO = vDao;

        setLayout(new BorderLayout());
        setBackground(new Color(245, 246, 250)); 
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // --- TOPO ---
        JPanel topHeader = new JPanel(new BorderLayout());
        topHeader.setBackground(new Color(245, 246, 250));
        topHeader.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel lblTitulo = new JLabel("Visão Geral");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        
        cbFiltro = new JComboBox<>(new String[]{"Hoje", "Esta Semana", "Este Mês", "Este Ano", "Total Geral"});
        cbFiltro.setSelectedIndex(2);
        cbFiltro.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbFiltro.setPreferredSize(new Dimension(150, 30));
        cbFiltro.addActionListener(e -> atualizarDados());

        topHeader.add(lblTitulo, BorderLayout.WEST);
        topHeader.add(cbFiltro, BorderLayout.EAST);
        
        // --- CARDS ---
        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        cardsPanel.setBackground(new Color(245, 246, 250));
        cardsPanel.setPreferredSize(new Dimension(0, 130));

        lblFaturamento = new JLabel("R$ 0,00");
        lblValorEstoque = new JLabel("...");
        lblVendas = new JLabel("0");
        
        // Inicializa Labels de Título
        lblTituloFat = new JLabel("FATURAMENTO");
        lblTituloVendas = new JLabel("VENDAS REALIZADAS");

        cardsPanel.add(criarCard(lblTituloFat, lblFaturamento, new Color(40, 167, 69)));
        cardsPanel.add(criarCard(new JLabel("VALOR EM ESTOQUE (Atual)"), lblValorEstoque, new Color(0, 123, 255)));
        cardsPanel.add(criarCard(lblTituloVendas, lblVendas, new Color(23, 162, 184)));

        // --- RANKING ---
        JPanel rankingPanel = new JPanel(new BorderLayout());
        rankingPanel.setBackground(Color.WHITE);
        rankingPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        lblTituloRank = new JLabel("Ranking de Produtos Mais Vendidos");
        lblTituloRank.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTituloRank.setForeground(new Color(50, 50, 50));
        lblTituloRank.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        String[] colunas = {"Posição", "Produto", "Quantidade"};
        modeloRanking = new DefaultTableModel(colunas, 0) {
             @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabelaRank = new JTable(modeloRanking);
        tabelaRank.setRowHeight(40);
        tabelaRank.setShowVerticalLines(false);
        tabelaRank.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabelaRank.getTableHeader().setBackground(new Color(248, 249, 250));
        
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        tabelaRank.getColumnModel().getColumn(0).setCellRenderer(center);
        tabelaRank.getColumnModel().getColumn(2).setCellRenderer(center);
        
        rankingPanel.add(lblTituloRank, BorderLayout.NORTH);
        rankingPanel.add(new JScrollPane(tabelaRank), BorderLayout.CENTER);

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setBackground(new Color(245, 246, 250));
        topContainer.add(topHeader, BorderLayout.NORTH);
        topContainer.add(cardsPanel, BorderLayout.CENTER);
        topContainer.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0)); 

        add(topContainer, BorderLayout.NORTH);
        add(rankingPanel, BorderLayout.CENTER);
        
        SwingUtilities.invokeLater(this::atualizarDados);
    }

    public void atualizarDados() {
        try {
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));
            LocalDateTime agora = LocalDateTime.now().with(LocalTime.MAX);
            LocalDateTime inicio = agora.with(LocalTime.MIN);
            
            String textoFiltro = (String) cbFiltro.getSelectedItem();
            boolean filtroGeral = false;

            // Define Datas e Texto
            switch (cbFiltro.getSelectedIndex()) {
                case 0: inicio = agora.with(LocalTime.MIN); break; // Hoje
                case 1: inicio = agora.minusDays(7).with(LocalTime.MIN); break; // Semana
                case 2: inicio = agora.withDayOfMonth(1).with(LocalTime.MIN); break; // Mês
                case 3: inicio = agora.withDayOfYear(1).with(LocalTime.MIN); break; // Ano
                case 4: filtroGeral = true; break; // Total
            }
            
            // Debug no Console (Olhe no VS Code se as datas mudam)
            System.out.println("Filtro: " + textoFiltro);
            System.out.println("Inicio: " + inicio + " | Fim: " + agora);

            // Atualiza Títulos Visualmente
            lblTituloFat.setText("FATURAMENTO (" + textoFiltro + ")");
            lblTituloVendas.setText("VENDAS (" + textoFiltro + ")");
            lblTituloRank.setText("Ranking de Produtos (" + textoFiltro + ")");

            // Busca Dados
            BigDecimal faturamento;
            long qtdVendas;
            List<Object[]> ranking;

            if (filtroGeral) {
                faturamento = vendaDAO.calcularTotalFaturamento();
                qtdVendas = vendaDAO.count();
                ranking = vendaItemDAO.buscarRankingMaisVendidos();
            } else {
                faturamento = vendaDAO.calcularFaturamentoPorPeriodo(inicio, agora);
                qtdVendas = vendaDAO.countByDataHoraBetween(inicio, agora);
                ranking = vendaItemDAO.buscarRankingPorPeriodo(inicio, agora);
            }

            lblFaturamento.setText(nf.format(faturamento));
            lblVendas.setText(String.valueOf(qtdVendas));

            // Estoque (Sempre Geral)
            BigDecimal totalEstoque = produtoDAO.calcularValorTotalEstoque();
            if (totalEstoque == null) totalEstoque = BigDecimal.ZERO;
            lblValorEstoque.setText(nf.format(totalEstoque));
            
            // Ranking
            modeloRanking.setRowCount(0);
            if (ranking.isEmpty()) {
                 modeloRanking.addRow(new Object[]{"-", "Sem dados neste período", "-"});
            } else {
                int pos = 1;
                for (Object[] item : ranking) {
                    modeloRanking.addRow(new Object[]{pos + "º", item[0], item[1] + " un."});
                    pos++;
                }
            }
            
            // Força atualização da tela
            this.revalidate();
            this.repaint();

        } catch (Exception e) {
            e.printStackTrace();
            lblFaturamento.setText("Erro");
        }
    }

    private JPanel criarCard(JLabel lblT, JLabel labelValor, Color cor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, cor));
        
        JPanel conteudo = new JPanel(new GridLayout(2, 1));
        conteudo.setBackground(Color.WHITE);
        conteudo.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        lblT.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblT.setForeground(Color.GRAY);
        
        labelValor.setFont(new Font("Segoe UI", Font.BOLD, 26));
        labelValor.setForeground(cor);

        conteudo.add(lblT);
        conteudo.add(labelValor);
        card.add(conteudo, BorderLayout.CENTER);
        
        JPanel shadow = new JPanel(new BorderLayout());
        shadow.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        shadow.add(card);
        return shadow;
    }
}