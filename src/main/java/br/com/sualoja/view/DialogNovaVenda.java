package br.com.sualoja.view;

import br.com.sualoja.controller.VendaController;
import br.com.sualoja.dao.ClienteDAO;
import br.com.sualoja.dao.ProdutoDAO;
import br.com.sualoja.model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DialogNovaVenda extends JDialog {

    private VendaController vendaController;
    private ClienteDAO clienteDAO;
    private ProdutoDAO produtoDAO;
    private Usuario usuarioLogado;
    
    private JComboBox<Cliente> cbClientes;
    private JComboBox<Produto> cbProdutos;
    private JSpinner txtQtd;
    private JTable tabelaItens;
    private DefaultTableModel modeloItens;
    private JLabel lblTotal;
    
    private List<VendaItem> itensCarrinho = new ArrayList<>();
    private BigDecimal totalVenda = BigDecimal.ZERO;

    public DialogNovaVenda(Frame parent, VendaController vc, ClienteDAO cd, ProdutoDAO pd, Usuario user) {
        super(parent, "Nova Venda - PDV", true);
        this.vendaController = vc;
        this.clienteDAO = cd;
        this.produtoDAO = pd;
        this.usuarioLogado = user;

        setSize(1000, 650);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel panelLeft = new JPanel();
        panelLeft.setLayout(new BoxLayout(panelLeft, BoxLayout.Y_AXIS));
        panelLeft.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelLeft.setPreferredSize(new Dimension(350, 0));
        panelLeft.setBackground(Color.WHITE);

        panelLeft.add(criarLabel("Selecione o Cliente:"));
        JPanel pCliente = new JPanel(new BorderLayout(5, 0));
        pCliente.setBackground(Color.WHITE);
        pCliente.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        
        cbClientes = new JComboBox<>();
        carregarClientes();
        
        JButton btnNovoCliente = new JButton("+");
        btnNovoCliente.addActionListener(e -> cadastrarClienteRapido());
        
        pCliente.add(cbClientes, BorderLayout.CENTER);
        pCliente.add(btnNovoCliente, BorderLayout.EAST);
        panelLeft.add(pCliente);
        
        panelLeft.add(Box.createRigidArea(new Dimension(0, 20)));

        panelLeft.add(criarLabel("Selecione o Produto:"));
        cbProdutos = new JComboBox<>();
        cbProdutos.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        
        cbProdutos.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Produto) {
                    Produto p = (Produto) value;
                    if (p.getQuantidadeEstoque() <= 0) {
                        setText(p.getNome() + " (EM FALTA)");
                        setForeground(Color.RED);
                    } else {
                        setText(p.getNome() + " [Estoque: " + p.getQuantidadeEstoque() + "]");
                        setForeground(Color.BLACK);
                    }
                }
                if (isSelected) {
                    setBackground(list.getSelectionBackground());
                    setForeground(list.getSelectionForeground());
                }
                return c;
            }
        });

        carregarProdutos();
        panelLeft.add(cbProdutos);

        panelLeft.add(Box.createRigidArea(new Dimension(0, 20)));

        panelLeft.add(criarLabel("Quantidade:"));
        txtQtd = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        txtQtd.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        JFormattedTextField tf = ((JSpinner.DefaultEditor) txtQtd.getEditor()).getTextField();
        tf.setHorizontalAlignment(JTextField.LEFT);
        panelLeft.add(txtQtd);

        panelLeft.add(Box.createRigidArea(new Dimension(0, 30)));

        JButton btnAdicionar = new JButton("ADICIONAR AO CARRINHO ⬇");
        btnAdicionar.setBackground(new Color(0, 123, 255));
        btnAdicionar.setForeground(Color.WHITE);
        btnAdicionar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAdicionar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnAdicionar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdicionar.addActionListener(e -> adicionarItem());
        
        panelLeft.add(btnAdicionar);
        panelLeft.add(Box.createVerticalGlue());

        JPanel panelRight = new JPanel(new BorderLayout());
        panelRight.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 20));
        
        String[] cols = {"Produto", "Qtd", "Preço Un.", "Subtotal"};
        modeloItens = new DefaultTableModel(cols, 0);
        tabelaItens = new JTable(modeloItens);
        tabelaItens.setRowHeight(30);
        tabelaItens.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JScrollPane scroll = new JScrollPane(tabelaItens);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200,200,200)));

        JPanel pBaixo = new JPanel(new BorderLayout());
        pBaixo.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        lblTotal = new JLabel("Total: R$ 0,00");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
        lblTotal.setForeground(new Color(40, 167, 69));
        
        JButton btnFinalizar = new JButton("CONCLUIR VENDA (F5)");
        btnFinalizar.setBackground(new Color(40, 167, 69));
        btnFinalizar.setForeground(Color.WHITE);
        btnFinalizar.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnFinalizar.setPreferredSize(new Dimension(0, 60));
        btnFinalizar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnFinalizar.addActionListener(e -> finalizarVenda());

        pBaixo.add(lblTotal, BorderLayout.NORTH);
        pBaixo.add(btnFinalizar, BorderLayout.SOUTH);

        panelRight.add(new JLabel("Itens no Carrinho:"), BorderLayout.NORTH);
        panelRight.add(scroll, BorderLayout.CENTER);
        panelRight.add(pBaixo, BorderLayout.SOUTH);

        add(panelLeft, BorderLayout.WEST);
        add(panelRight, BorderLayout.CENTER);
    }

    private JLabel criarLabel(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("Segoe UI", Font.BOLD, 14));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        l.setBorder(BorderFactory.createEmptyBorder(0,0,5,0));
        return l;
    }

    private void carregarClientes() {
        cbClientes.removeAllItems();
        // MÉTODO ATUALIZADO
        List<Cliente> lista = clienteDAO.findAllByOrderByNomeAsc();
        for(Cliente c : lista) cbClientes.addItem(c);
    }

    private void carregarProdutos() {
        cbProdutos.removeAllItems();
        // MÉTODO ATUALIZADO
        List<Produto> lista = produtoDAO.findByAtivoTrueOrderByNomeAsc();
        for(Produto p : lista) cbProdutos.addItem(p);
    }

    private void cadastrarClienteRapido() {
        String nome = JOptionPane.showInputDialog(this, "Nome do Cliente:");
        if(nome != null && !nome.isEmpty()) {
            Cliente c = new Cliente();
            c.setNome(nome);
            c.setCpf("000"); 
            c.setEndereco("-");
            c.setTelefone("-");
            clienteDAO.save(c);
            carregarClientes(); 
            cbClientes.setSelectedIndex(cbClientes.getItemCount()-1);
        }
    }

    private void adicionarItem() {
        try {
            Produto p = (Produto) cbProdutos.getSelectedItem();
            int qtd = (Integer) txtQtd.getValue();
            
            if (p == null) return;
            
            if (p.getQuantidadeEstoque() <= 0) {
                JOptionPane.showMessageDialog(this, "Produto EM FALTA! Não é possível vender.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (qtd > p.getQuantidadeEstoque()) {
                JOptionPane.showMessageDialog(this, "Estoque insuficiente! Disponível: " + p.getQuantidadeEstoque());
                return;
            }

            BigDecimal subtotal = p.getPrecoVenda().multiply(new BigDecimal(qtd));
            
            VendaItem item = new VendaItem();
            item.setProduto(p);
            item.setQuantidade(qtd);
            item.setPrecoUnitario(p.getPrecoVenda());
            
            itensCarrinho.add(item);
            totalVenda = totalVenda.add(subtotal);
            
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));
            modeloItens.addRow(new Object[]{p.getNome(), qtd, nf.format(p.getPrecoVenda()), nf.format(subtotal)});
            
            lblTotal.setText("Total: " + nf.format(totalVenda));
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao adicionar item.");
        }
    }

    private void finalizarVenda() {
        if(itensCarrinho.isEmpty()) {
            JOptionPane.showMessageDialog(this, "O carrinho está vazio!");
            return;
        }
        try {
            Cliente cliente = (Cliente) cbClientes.getSelectedItem();
            
            Venda venda = new Venda();
            venda.setCliente(cliente);
            venda.setVendedor(usuarioLogado); 
            venda.setValorTotal(totalVenda);
            
            vendaController.realizarVenda(venda, itensCarrinho);
            
            JOptionPane.showMessageDialog(this, "Venda Sucesso!");
            dispose();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao finalizar: " + e.getMessage());
            e.printStackTrace();
        }
    }
}