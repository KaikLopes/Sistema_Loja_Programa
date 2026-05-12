package br.com.sualoja.view;

import br.com.sualoja.controller.DashboardController;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class PanelDashboard extends JPanel {

    private DashboardController dashboardController; // <-- Apenas o Controller!

    private JLabel lblValorEstoque, lblFaturamento, lblVendas;
    private JLabel lblTituloFat, lblTituloVendas, lblTituloRank;
    private DefaultTableModel modeloRanking;
    private JComboBox<String> cbFiltro;

    public PanelDashboard(DashboardController controller) {
        this.dashboardController = controller;

        setLayout(new BorderLayout());
        setBackground(new Color(245, 246, 250));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // --- TOPO ---
        JPanel topHeader = new JPanel(new BorderLayout());
        topHeader.setBackground(new Color(245, 246, 250));
        JLabel lblTitulo = new JLabel("Visão Geral");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));

        cbFiltro = new JComboBox<>(new String[]{"Hoje", "Esta Semana", "Este Mês", "Este Ano", "Total Geral"});
        cbFiltro.setSelectedIndex(2);
        cbFiltro.addActionListener(e -> atualizarDados());
        topHeader.add(lblTitulo, BorderLayout.WEST);
        topHeader.add(cbFiltro, BorderLayout.EAST);

        // --- CARDS ---
        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        cardsPanel.setBackground(new Color(245, 246, 250));
        cardsPanel.setPreferredSize(new Dimension(0, 130));

        lblFaturamento = new JLabel("...");
        lblValorEstoque = new JLabel("...");
        lblVendas = new JLabel("...");

        lblTituloFat = new JLabel("FATURAMENTO");
        lblTituloVendas = new JLabel("VENDAS REALIZADAS");

        cardsPanel.add(criarCard(lblTituloFat, lblFaturamento, new Color(40, 167, 69)));
        cardsPanel.add(criarCard(new JLabel("VALOR EM ESTOQUE (Geral)"), lblValorEstoque, new Color(0, 123, 255)));
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

        String[] colunas = {"Posição", "Produto", "Quantidade"};
        modeloRanking = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabelaRank = new JTable(modeloRanking);
        tabelaRank.setRowHeight(40);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        tabelaRank.getColumnModel().getColumn(0).setCellRenderer(center);
        tabelaRank.getColumnModel().getColumn(2).setCellRenderer(center);

        rankingPanel.add(lblTituloRank, BorderLayout.NORTH);
        rankingPanel.add(new JScrollPane(tabelaRank), BorderLayout.CENTER);

        add(topHeader, BorderLayout.NORTH);
        add(cardsPanel, BorderLayout.CENTER); // Simplificado para o exemplo

        // Montagem final do layout
        JPanel mainTop = new JPanel(new BorderLayout());
        mainTop.setBackground(new Color(245, 246, 250));
        mainTop.add(topHeader, BorderLayout.NORTH);
        mainTop.add(cardsPanel, BorderLayout.CENTER);
        mainTop.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        add(mainTop, BorderLayout.NORTH);
        add(rankingPanel, BorderLayout.CENTER);

        SwingUtilities.invokeLater(this::atualizarDados);
    }

    public void atualizarDados() {
        int index = cbFiltro.getSelectedIndex();
        String texto = (String) cbFiltro.getSelectedItem();

        lblTituloFat.setText("FATURAMENTO (" + texto + ")");
        lblTituloVendas.setText("VENDAS (" + texto + ")");

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            BigDecimal fat, estoque;
            long vendas;
            List<Object[]> rank;

            @Override protected Void doInBackground() {
                fat = dashboardController.getFaturamento(index);
                vendas = dashboardController.getQtdVendas(index);
                estoque = dashboardController.getValorEstoque();
                rank = dashboardController.getRanking(index);
                return null;
            }

            @Override protected void done() {
                try {
                    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));
                    lblFaturamento.setText(nf.format(fat));
                    lblVendas.setText(String.valueOf(vendas));
                    lblValorEstoque.setText(nf.format(estoque));

                    modeloRanking.setRowCount(0);
                    int pos = 1;
                    for (Object[] item : rank) {
                        modeloRanking.addRow(new Object[]{pos + "º", item[0], item[1] + " un."});
                        pos++;
                    }
                } catch (Exception e) { e.printStackTrace(); }
            }
        };
        worker.execute();
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
        return card;
    }
}