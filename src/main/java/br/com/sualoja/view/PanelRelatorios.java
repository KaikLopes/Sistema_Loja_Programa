package br.com.sualoja.view;

import br.com.sualoja.controller.RelatorioController;
import br.com.sualoja.model.Produto;
import br.com.sualoja.model.Venda;
import br.com.sualoja.util.FormatUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PanelRelatorios extends JPanel {

    private RelatorioController relatorioController;
    private JLabel lblEntradas, lblSaidas, lblLucro;
    private JComboBox<String> cbFiltro;

    private List<Venda> vendasPeriodo = new ArrayList<>();
    private List<Produto> produtosPeriodo = new ArrayList<>();

    public PanelRelatorios(RelatorioController controller) {
        this.relatorioController = controller;

        setLayout(new BorderLayout());
        setBackground(new Color(245, 246, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(new Color(245, 246, 250));
        JLabel lbl = new JLabel("Fluxo de Caixa");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 24));

        cbFiltro = new JComboBox<>(new String[]{"Hoje", "Esta Semana", "Este Mês", "Este Ano"});
        cbFiltro.setSelectedIndex(2);
        cbFiltro.addActionListener(e -> calcularRelatorio());

        top.add(lbl);
        top.add(Box.createHorizontalStrut(20));
        top.add(cbFiltro);

        JPanel centro = new JPanel(new GridLayout(1, 3, 30, 0));
        centro.setBackground(new Color(245, 246, 250));
        centro.setBorder(BorderFactory.createEmptyBorder(30, 0, 150, 0));

        lblEntradas = new JLabel("...");
        lblSaidas = new JLabel("...");
        lblLucro = new JLabel("...");

        centro.add(criarCard("VENDAS (Entrada)", lblEntradas, new Color(40, 167, 69), "ENTRADA"));
        centro.add(criarCard("COMPRAS (Saída)", lblSaidas, new Color(220, 53, 69), "SAIDA"));
        centro.add(criarCard("SALDO", lblLucro, new Color(111, 66, 193), "LUCRO"));

        add(top, BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);

        SwingUtilities.invokeLater(this::calcularRelatorio);
    }

    public void calcularRelatorio() {
        int index = cbFiltro.getSelectedIndex();
        lblEntradas.setText("Calculando...");
        lblSaidas.setText("Calculando...");
        lblLucro.setText("...");

        SwingWorker<BigDecimal[], Void> worker = new SwingWorker<>() {
            @Override
            protected BigDecimal[] doInBackground() {
                vendasPeriodo = relatorioController.buscarVendasNoPeriodo(index);
                produtosPeriodo = relatorioController.buscarProdutosNoPeriodo(index);

                BigDecimal totalEntrada = relatorioController.calcularTotalEntradas(vendasPeriodo);
                BigDecimal totalSaida = relatorioController.calcularTotalSaidas(produtosPeriodo);
                BigDecimal lucro = totalEntrada.subtract(totalSaida);

                return new BigDecimal[]{totalEntrada, totalSaida, lucro};
            }

            @Override
            protected void done() {
                try {
                    BigDecimal[] resultados = get();
                    lblEntradas.setText(FormatUtils.formatarMoeda(resultados[0]));
                    lblSaidas.setText(FormatUtils.formatarMoeda(resultados[1]));
                    lblLucro.setText(FormatUtils.formatarMoeda(resultados[2]));
                } catch (Exception e) {
                    lblLucro.setText("Erro");
                }
            }
        };
        worker.execute();
    }

    private JPanel criarCard(String titulo, JLabel lblValor, Color cor, String tipo) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createMatteBorder(0, 8, 0, 0, cor));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel content = new JPanel(new GridLayout(2, 1));
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel t = new JLabel(titulo);
        t.setFont(new Font("Segoe UI", Font.BOLD, 14));
        t.setForeground(Color.GRAY);

        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblValor.setForeground(cor);

        content.add(t);
        content.add(lblValor);
        card.add(content, BorderLayout.CENTER);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) { abrirDetalhes(tipo); }
        });

        JPanel shadow = new JPanel(new BorderLayout());
        shadow.setBorder(BorderFactory.createLineBorder(new Color(220,220,220)));
        shadow.add(card);
        return shadow;
    }

    private void abrirDetalhes(String tipo) {
        JDialog d = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Detalhes " + tipo, true);
        d.setSize(900, 500);
        d.setLocationRelativeTo(this);

        DefaultTableModel model;
        if (tipo.equals("ENTRADA")) {
            String[] cols = {"Data", "Cliente", "Valor"};
            model = new DefaultTableModel(cols, 0);
            for(Venda v : vendasPeriodo) {
                model.addRow(new Object[]{FormatUtils.formatarDataHora(v.getDataHora()), v.getCliente().getNome(), FormatUtils.formatarMoeda(v.getValorTotal())});
            }
        } else if (tipo.equals("SAIDA")) {
            String[] cols = {"Data", "Produto", "Fornecedor", "Qtd", "Custo Un.", "Total"};
            model = new DefaultTableModel(cols, 0);
            for(Produto p : produtosPeriodo) {
                BigDecimal custo = (p.getUltimoPrecoCompra() != null) ? p.getUltimoPrecoCompra() : BigDecimal.ZERO;
                Integer qtd = (p.getQuantidadeInicial() != null) ? p.getQuantidadeInicial() : 0;
                String forn = (p.getFornecedor() != null) ? p.getFornecedor().getNomeFantasia() : "-";
                model.addRow(new Object[]{FormatUtils.formatarData(p.getCriadoEm()), p.getNome(), forn, qtd, FormatUtils.formatarMoeda(custo), FormatUtils.formatarMoeda(custo.multiply(new BigDecimal(qtd)))});
            }
        } else {
            String[] cols = {"Resumo", "Valor"};
            model = new DefaultTableModel(cols, 0);
            model.addRow(new Object[]{"Total Vendas", lblEntradas.getText()});
            model.addRow(new Object[]{"Total Compras", lblSaidas.getText()});
            model.addRow(new Object[]{"SALDO", lblLucro.getText()});
        }

        JTable table = new JTable(model);
        table.setRowHeight(30);
        d.add(new JScrollPane(table));
        d.setVisible(true);
    }
}