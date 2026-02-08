package br.com.sualoja.view;

import br.com.sualoja.dao.ProdutoDAO;
import br.com.sualoja.dao.VendaDAO;
import br.com.sualoja.model.Produto;
import br.com.sualoja.model.Venda;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PanelRelatorios extends JPanel {

    private VendaDAO vendaDAO;
    private ProdutoDAO produtoDAO;
    private JLabel lblEntradas, lblSaidas, lblLucro;
    private JComboBox<String> cbFiltro;
    
    private List<Venda> vendasPeriodo = new ArrayList<>();
    private List<Produto> produtosPeriodo = new ArrayList<>();

    public PanelRelatorios(VendaDAO vendaDAO, ProdutoDAO produtoDAO) {
        this.vendaDAO = vendaDAO;
        this.produtoDAO = produtoDAO;
        
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

        lblEntradas = new JLabel("R$ 0,00");
        lblSaidas = new JLabel("R$ 0,00");
        lblLucro = new JLabel("R$ 0,00");

        centro.add(criarCard("VENDAS (Entrada)", lblEntradas, new Color(40, 167, 69), "ENTRADA"));
        centro.add(criarCard("COMPRAS (Saída)", lblSaidas, new Color(220, 53, 69), "SAIDA"));
        centro.add(criarCard("SALDO", lblLucro, new Color(111, 66, 193), "LUCRO"));

        add(top, BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);
        
        SwingUtilities.invokeLater(this::calcularRelatorio);
    }

    public void calcularRelatorio() {
        try {
            LocalDateTime agora = LocalDateTime.now().with(LocalTime.MAX);
            LocalDateTime inicio = agora.with(LocalTime.MIN);

            switch (cbFiltro.getSelectedIndex()) {
                case 0: inicio = LocalDateTime.now().with(LocalTime.MIN); break;
                case 1: inicio = LocalDateTime.now().minusDays(7).with(LocalTime.MIN); break;
                case 2: inicio = LocalDateTime.now().withDayOfMonth(1).with(LocalTime.MIN); break;
                case 3: inicio = LocalDateTime.now().withDayOfYear(1).with(LocalTime.MIN); break;
            }

            vendasPeriodo = vendaDAO.findByDataHoraBetween(inicio, agora);
            if(vendasPeriodo == null) vendasPeriodo = new ArrayList<>();
            
            BigDecimal totalEntrada = BigDecimal.ZERO;
            for (Venda v : vendasPeriodo) {
                if(v.getValorTotal() != null) totalEntrada = totalEntrada.add(v.getValorTotal());
            }

            produtosPeriodo = produtoDAO.findByCriadoEmBetween(inicio, agora);
            if(produtosPeriodo == null) produtosPeriodo = new ArrayList<>();
            
            BigDecimal totalSaida = BigDecimal.ZERO;
            for (Produto p : produtosPeriodo) {
                BigDecimal custo = (p.getUltimoPrecoCompra() != null) ? p.getUltimoPrecoCompra() : BigDecimal.ZERO;
                Integer qtd = (p.getQuantidadeInicial() != null) ? p.getQuantidadeInicial() : p.getQuantidadeEstoque();
                if(qtd == null) qtd = 0;
                
                totalSaida = totalSaida.add(custo.multiply(new BigDecimal(qtd)));
            }

            BigDecimal lucro = totalEntrada.subtract(totalSaida);
            
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));
            lblEntradas.setText(nf.format(totalEntrada));
            lblSaidas.setText(nf.format(totalSaida));
            lblLucro.setText(nf.format(lucro));

        } catch (Exception e) {
            e.printStackTrace();
        }
        revalidate();
        repaint();
    }

    private JPanel criarCard(String titulo, JLabel lblValor, Color cor, String tipo) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createMatteBorder(0, 8, 0, 0, cor));
        p.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
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
        p.add(content, BorderLayout.CENTER);
        
        JPanel shadow = new JPanel(new BorderLayout());
        shadow.setBorder(BorderFactory.createLineBorder(new Color(220,220,220)));
        shadow.add(p);

        MouseAdapter click = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { abrirDetalhes(tipo); }
        };
        p.addMouseListener(click);
        content.addMouseListener(click);
        lblValor.addMouseListener(click);
        t.addMouseListener(click);

        return shadow;
    }

    private void abrirDetalhes(String tipo) {
        JDialog d = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Detalhes " + tipo, true);
        d.setSize(900, 500);
        d.setLocationRelativeTo(this);
        
        DefaultTableModel model;
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM HH:mm");

        if (tipo.equals("ENTRADA")) {
            String[] cols = {"Data", "Cliente", "Valor"};
            model = new DefaultTableModel(cols, 0);
            for(Venda v : vendasPeriodo) {
                model.addRow(new Object[]{v.getDataHora().format(dtf), v.getCliente().getNome(), nf.format(v.getValorTotal())});
            }
        } else if (tipo.equals("SAIDA")) {
            String[] cols = {"Data", "Produto", "Fornecedor", "Qtd Comprada", "Custo Un.", "Total"};
            model = new DefaultTableModel(cols, 0);
            for(Produto p : produtosPeriodo) {
                BigDecimal custo = (p.getUltimoPrecoCompra() != null) ? p.getUltimoPrecoCompra() : BigDecimal.ZERO;
                Integer qtd = (p.getQuantidadeInicial() != null) ? p.getQuantidadeInicial() : 0;
                String forn = (p.getFornecedor() != null) ? p.getFornecedor().getNomeFantasia() : "-";
                
                model.addRow(new Object[]{
                    p.getCriadoEm().format(dtf), p.getNome(), forn, qtd, nf.format(custo), nf.format(custo.multiply(new BigDecimal(qtd)))
                });
            }
        } else {
            String[] cols = {"Resumo", "Valor"};
            model = new DefaultTableModel(cols, 0);
            model.addRow(new Object[]{"Total Vendas", lblEntradas.getText()});
            model.addRow(new Object[]{"Total Compras", lblSaidas.getText()});
            model.addRow(new Object[]{"LUCRO", lblLucro.getText()});
        }

        JTable table = new JTable(model);
        table.setRowHeight(30);
        d.add(new JScrollPane(table));
        d.setVisible(true);
    }
}