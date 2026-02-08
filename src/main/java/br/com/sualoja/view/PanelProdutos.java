package br.com.sualoja.view;

import br.com.sualoja.controller.ProdutoController;
import br.com.sualoja.dao.CategoriaDAO; // <--- NOVO
import br.com.sualoja.dao.FornecedorDAO;
import br.com.sualoja.dao.ProdutoDAO;
import br.com.sualoja.model.Categoria; // <--- NOVO
import br.com.sualoja.model.Fornecedor;
import br.com.sualoja.model.Produto;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class PanelProdutos extends JPanel {

    private ProdutoController controller;
    private FornecedorDAO fornecedorDAO;
    private CategoriaDAO categoriaDAO; // <--- NOVO
    private ProdutoDAO produtoDAO;
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    // Construtor atualizado recebendo CategoriaDAO
    public PanelProdutos(ProdutoController controller, FornecedorDAO fornecedorDAO, CategoriaDAO categoriaDAO, ProdutoDAO produtoDAO) {
        this.controller = controller;
        this.fornecedorDAO = fornecedorDAO;
        this.categoriaDAO = categoriaDAO; // <--- GUARDANDO
        this.produtoDAO = produtoDAO;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 246, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 246, 250));
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JLabel lblTitulo = new JLabel("Gerenciamento de Produtos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JButton btnNovo = new JButton("+ Novo Produto");
        btnNovo.setBackground(new Color(0, 123, 255));
        btnNovo.setForeground(Color.WHITE);
        btnNovo.addActionListener(e -> abrirFormulario(null));

        topPanel.add(lblTitulo, BorderLayout.WEST);
        topPanel.add(btnNovo, BorderLayout.EAST);

        criarTabelaModerna();

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(245, 246, 250));
        JButton btnEditar = new JButton("Editar");
        btnEditar.setBackground(new Color(255, 193, 7));
        btnEditar.addActionListener(e -> editarSelecionado());
        
        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setBackground(new Color(220, 53, 69));
        btnExcluir.setForeground(Color.WHITE);
        btnExcluir.addActionListener(e -> excluirSelecionado());

        bottomPanel.add(btnEditar);
        bottomPanel.add(btnExcluir);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        carregarDados();
    }

    private void abrirFormulario(Produto produto) {
        boolean edicao = (produto != null);
        JDialog d = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), edicao ? "Editar" : "Novo Produto", true);
        d.setSize(600, 500); // Aumentei um pouco
        d.setLayout(new GridLayout(7, 2, 10, 10)); // +1 linha para categoria
        d.setLocationRelativeTo(this);

        JTextField txtNome = new JTextField(edicao ? produto.getNome() : "");
        JTextField txtPrecoVenda = new JTextField(edicao ? produto.getPrecoVenda().toString() : "");
        JTextField txtPrecoCompra = new JTextField(edicao && produto.getUltimoPrecoCompra() != null ? produto.getUltimoPrecoCompra().toString() : "");
        JTextField txtQtd = new JTextField(edicao ? produto.getQuantidadeEstoque().toString() : "");
        
        // FORNECEDOR
        JPanel pForn = new JPanel(new BorderLayout());
        JComboBox<Fornecedor> cbFornecedor = new JComboBox<>();
        carregarFornecedores(cbFornecedor);
        if(edicao && produto.getFornecedor() != null) cbFornecedor.setSelectedItem(produto.getFornecedor());
        
        JButton btnAddForn = new JButton("+");
        btnAddForn.addActionListener(e -> {
            String nome = JOptionPane.showInputDialog("Nome do Fornecedor:");
            if(nome != null && !nome.isEmpty()) {
                Fornecedor f = new Fornecedor(nome, "000");
                fornecedorDAO.save(f);
                carregarFornecedores(cbFornecedor);
                cbFornecedor.setSelectedItem(f);
            }
        });
        pForn.add(cbFornecedor, BorderLayout.CENTER);
        pForn.add(btnAddForn, BorderLayout.EAST);

        // --- NOVO: CATEGORIA ---
        JPanel pCat = new JPanel(new BorderLayout());
        JComboBox<Categoria> cbCategoria = new JComboBox<>();
        carregarCategorias(cbCategoria);
        if(edicao && produto.getCategoria() != null) cbCategoria.setSelectedItem(produto.getCategoria());

        JButton btnAddCat = new JButton("+");
        btnAddCat.addActionListener(e -> {
            String nome = JOptionPane.showInputDialog("Nome da Categoria:");
            if(nome != null && !nome.isEmpty()) {
                Categoria c = new Categoria(nome);
                categoriaDAO.save(c);
                carregarCategorias(cbCategoria);
                cbCategoria.setSelectedItem(c);
            }
        });
        pCat.add(cbCategoria, BorderLayout.CENTER);
        pCat.add(btnAddCat, BorderLayout.EAST);

        // Adiciona Campos
        d.add(new JLabel(" Nome:")); d.add(txtNome);
        d.add(new JLabel(" Categoria:")); d.add(pCat); // NOVO
        d.add(new JLabel(" Preço Venda (R$):")); d.add(txtPrecoVenda);
        d.add(new JLabel(" Preço Compra (R$):")); d.add(txtPrecoCompra);
        d.add(new JLabel(" Estoque Atual:")); d.add(txtQtd);
        d.add(new JLabel(" Fornecedor:")); d.add(pForn);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setBackground(new Color(40, 167, 69));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.addActionListener(e -> {
            try {
                Produto p = edicao ? produto : new Produto();
                p.setNome(txtNome.getText());
                p.setPrecoVenda(new BigDecimal(txtPrecoVenda.getText().replace(",", ".")));
                
                String compra = txtPrecoCompra.getText().replace(",", ".");
                if(!compra.isEmpty()) p.setUltimoPrecoCompra(new BigDecimal(compra));
                else p.setUltimoPrecoCompra(BigDecimal.ZERO);
                
                int qtd = Integer.parseInt(txtQtd.getText());
                p.setQuantidadeEstoque(qtd);
                if(!edicao) p.setQuantidadeInicial(qtd);
                
                p.setFornecedor((Fornecedor) cbFornecedor.getSelectedItem());
                p.setCategoria((Categoria) cbCategoria.getSelectedItem()); // SALVA CATEGORIA
                p.setAtivo(true); 
                
                produtoDAO.save(p);
                d.dispose();
                carregarDados();
                JOptionPane.showMessageDialog(this, "Sucesso!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(d, "Erro: " + ex.getMessage());
            }
        });

        d.add(new JLabel("")); d.add(btnSalvar);
        d.setVisible(true);
    }

    private void carregarFornecedores(JComboBox<Fornecedor> cb) {
        cb.removeAllItems();
        List<Fornecedor> lista = fornecedorDAO.findAllByOrderByNomeFantasiaAsc();
        for(Fornecedor f : lista) cb.addItem(f);
    }

    private void carregarCategorias(JComboBox<Categoria> cb) {
        cb.removeAllItems();
        List<Categoria> lista = categoriaDAO.findAllByOrderByNomeAsc();
        for(Categoria c : lista) cb.addItem(c);
    }

    public void carregarDados() {
        modeloTabela.setRowCount(0);
        List<Produto> lista = produtoDAO.findByAtivoTrueOrderByNomeAsc();
        for (Produto p : lista) {
            String nomeForn = (p.getFornecedor() != null) ? p.getFornecedor().getNomeFantasia() : "-";
            String nomeCat = (p.getCategoria() != null) ? p.getCategoria().getNome() : "-";
            
            modeloTabela.addRow(new Object[]{ 
                p.getId(), 
                p.getNome(), 
                nomeCat, // <--- Categoria na tabela
                p.getPrecoVenda(), 
                p.getQuantidadeEstoque(),
                nomeForn
            });
        }
    }
    
    private void criarTabelaModerna() {
        // Coluna Categoria adicionada
        String[] colunas = {"ID", "Nome", "Categoria", "Preço", "Estoque", "Fornecedor"};
        modeloTabela = new DefaultTableModel(colunas, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        tabela = new JTable(modeloTabela);
        tabela.setRowHeight(35);
        tabela.setShowVerticalLines(false);
        tabela.setGridColor(new Color(230,230,230));
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabela.getTableHeader().setBackground(Color.WHITE);
        
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        tabela.getColumnModel().getColumn(0).setCellRenderer(center);
        tabela.getColumnModel().getColumn(3).setCellRenderer(center); // Preço
        
        // Estoque Vermelho
        tabela.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.CENTER);
                int est = Integer.parseInt(value.toString());
                if(est <= 0) {
                    c.setForeground(Color.RED);
                    setText("EM FALTA");
                    setFont(new Font("Segoe UI", Font.BOLD, 12));
                } else {
                    c.setForeground(Color.BLACK);
                }
                if(isSelected) {
                    c.setBackground(table.getSelectionBackground());
                    c.setForeground(table.getSelectionForeground());
                } else {
                    c.setBackground(Color.WHITE);
                }
                return c;
            }
        });
    }

    private void editarSelecionado() {
        int linha = tabela.getSelectedRow();
        if(linha >= 0) {
            Integer id = (Integer) tabela.getValueAt(linha, 0);
            Produto p = produtoDAO.findById(id).orElse(null);
            if(p != null) abrirFormulario(p);
        }
    }
    
    private void excluirSelecionado() {
        int linha = tabela.getSelectedRow();
        if(linha >= 0) {
            if(JOptionPane.showConfirmDialog(this, "Tem certeza? O produto será arquivado.", "Excluir", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                Integer id = (Integer) tabela.getValueAt(linha, 0);
                Produto p = produtoDAO.findById(id).orElse(null);
                if(p != null) {
                    p.setAtivo(false);
                    produtoDAO.save(p);
                    carregarDados();
                }
            }
        }
    }
}