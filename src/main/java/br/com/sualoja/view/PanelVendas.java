package br.com.sualoja.view;

import br.com.sualoja.controller.VendaController;
import br.com.sualoja.model.Usuario;
import br.com.sualoja.model.Venda;
import br.com.sualoja.model.VendaItem;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class PanelVendas extends JPanel {

    private VendaController vendaController;
    private Usuario usuarioLogado;

    private JTable tabela;
    private DefaultTableModel modelo;
    private JTextField txtBusca;
    private JCheckBox chkOrdenarValor;

    public PanelVendas(VendaController vc, Usuario user) {
        this.vendaController = vc;
        this.usuarioLogado = user;

        setLayout(new BorderLayout());
        setBackground(new Color(245, 246, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(245, 246, 250));

        JLabel lblTitulo = new JLabel("Histórico");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));

        txtBusca = new JTextField(20);
        JButton btnBuscar = new JButton("Buscar");
        chkOrdenarValor = new JCheckBox("Ordenar por Valor");
        chkOrdenarValor.setBackground(new Color(245, 246, 250));

        JButton btnDetalhes = new JButton("Ver Detalhes");
        btnDetalhes.setBackground(new Color(23, 162, 184));
        btnDetalhes.setForeground(Color.WHITE);

        JButton btnNovaVenda = new JButton("+ NOVA VENDA");
        btnNovaVenda.setBackground(new Color(40, 167, 69));
        btnNovaVenda.setForeground(Color.WHITE);

        topPanel.add(lblTitulo);
        topPanel.add(txtBusca);
        topPanel.add(btnBuscar);
        topPanel.add(chkOrdenarValor);
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(btnDetalhes);
        topPanel.add(btnNovaVenda);

        String[] colunas = {"ID", "Data", "Cliente", "Vendedor", "Total (R$)"};
        modelo = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(modelo);
        tabela.setRowHeight(30);

        btnBuscar.addActionListener(e -> carregarVendas());
        chkOrdenarValor.addActionListener(e -> carregarVendas());

        btnNovaVenda.addActionListener(e -> {
            new DialogNovaVenda((Frame) SwingUtilities.getWindowAncestor(this), vendaController, usuarioLogado).setVisible(true);
            carregarVendas();
        });

        btnDetalhes.addActionListener(e -> verDetalhesVenda());

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        carregarVendas();
    }

    private void verDetalhesVenda() {
        int row = tabela.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma venda na tabela.");
            return;
        }
        Long idVenda = (Long) tabela.getValueAt(row, 0);
        List<VendaItem> itens = vendaController.buscarItensDaVenda(idVenda);

        StringBuilder sb = new StringBuilder();
        sb.append("ITENS DA VENDA #").append(idVenda).append("\n\n");
        sb.append(String.format("%-25s | %-5s | %-10s\n", "PRODUTO", "QTD", "PREÇO"));
        sb.append("--------------------------------------------------\n");
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));
        for(VendaItem item : itens) {
            sb.append(String.format("%-25s | %-5d | %-10s\n", item.getProduto().getNome(), item.getQuantidade(), nf.format(item.getPrecoUnitario())));
        }

        JTextArea area = new JTextArea(sb.toString());
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        area.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(area), "Detalhes da Venda", JOptionPane.PLAIN_MESSAGE);
    }

    public void carregarVendas() {
        modelo.setRowCount(0);
        modelo.addRow(new Object[]{"...", "Buscando histórico...", "", "", ""});

        SwingWorker<List<Venda>, Void> worker = new SwingWorker<>() {
            @Override protected List<Venda> doInBackground() {
                return vendaController.listarVendas(txtBusca.getText(), chkOrdenarValor.isSelected());
            }
            @Override protected void done() {
                try {
                    List<Venda> vendas = get();
                    modelo.setRowCount(0);
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));
                    for (Venda v : vendas) {
                        modelo.addRow(new Object[]{
                                v.getId(),
                                v.getDataHora() != null ? v.getDataHora().format(dtf) : "-",
                                v.getCliente().getNome(),
                                v.getVendedor() != null ? v.getVendedor().getNome() : "-",
                                nf.format(v.getValorTotal())
                        });
                    }
                } catch (Exception e) {
                    modelo.setRowCount(0);
                }
            }
        };
        worker.execute();
    }
}