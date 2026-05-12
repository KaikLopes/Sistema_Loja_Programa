package br.com.sualoja.view;

import br.com.sualoja.controller.ProdutoController;
import br.com.sualoja.model.Categoria;
import br.com.sualoja.model.Fornecedor;
import br.com.sualoja.model.Produto;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class PanelProdutos extends JPanel {

    // Apenas O Controller! Tchau, DAOs.
    private ProdutoController produtoController;
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    // Construtor atualizado: Aceita APENAS o ProdutoController
    public PanelProdutos(ProdutoController controller) {
        this.produtoController = controller;

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
        d.setSize(600, 500);
        d.setLayout(new GridLayout(7, 2, 10, 10));
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

        // O botão + foi removido daqui para simplificar a arquitetura.
        // O ideal é cadastrar o fornecedor na tela dele!
        pForn.add(cbFornecedor, BorderLayout.CENTER);

        // CATEGORIA
        JPanel pCat = new JPanel(new BorderLayout());
        JComboBox<Categoria> cbCategoria = new JComboBox<>();
        carregarCategorias(cbCategoria);
        if(edicao && produto.getCategoria() != null) cbCategoria.setSelectedItem(produto.getCategoria());

        pCat.add(cbCategoria, BorderLayout.CENTER);

        // Adiciona Campos
        d.add(new JLabel(" Nome:")); d.add(txtNome);
        d.add(new JLabel(" Categoria:")); d.add(pCat);
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

                // Trata a vírgula para não estourar NumberFormatException logo de cara
                String pVenda = txtPrecoVenda.getText().replace(",", ".");
                if(!pVenda.isEmpty()) p.setPrecoVenda(new BigDecimal(pVenda));

                String pCompra = txtPrecoCompra.getText().replace(",", ".");
                if(!pCompra.isEmpty()) p.setUltimoPrecoCompra(new BigDecimal(pCompra));
                else p.setUltimoPrecoCompra(BigDecimal.ZERO);

                // Validação de número inteiro para não quebrar a tela
                if(txtQtd.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(d, "A quantidade é obrigatória.");
                    return;
                }
                int qtd = Integer.parseInt(txtQtd.getText());
                p.setQuantidadeEstoque(qtd);
                if(!edicao) p.setQuantidadeInicial(qtd);

                p.setFornecedor((Fornecedor) cbFornecedor.getSelectedItem());
                p.setCategoria((Categoria) cbCategoria.getSelectedItem());
                p.setAtivo(true);

                // A MÁGICA AQUI: O Controller faz o trabalho!
                produtoController.salvar(p);

                d.dispose();
                carregarDados();
                JOptionPane.showMessageDialog(this, "Sucesso!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(d, "Erro: Digite apenas números válidos nos campos de preço e estoque.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(d, "Erro: " + ex.getMessage());
            }
        });

        d.add(new JLabel("")); d.add(btnSalvar);
        d.setVisible(true);
    }

    private void carregarFornecedores(JComboBox<Fornecedor> cb) {
        cb.removeAllItems();
        // O Controller busca!
        List<Fornecedor> lista = produtoController.buscarFornecedores();
        for(Fornecedor f : lista) cb.addItem(f);
    }

    private void carregarCategorias(JComboBox<Categoria> cb) {
        cb.removeAllItems();
        // O Controller busca!
        List<Categoria> lista = produtoController.buscarCategorias();
        for(Categoria c : lista) cb.addItem(c);
    }

    public void carregarDados() {
        modeloTabela.setRowCount(0);
        // O Controller busca!
        List<Produto> lista = produtoController.buscarTodos();
        for (Produto p : lista) {
            // Só mostra na tabela se estiver ativo
            if(p.getAtivo()) {
                String nomeForn = (p.getFornecedor() != null) ? p.getFornecedor().getNomeFantasia() : "-";
                String nomeCat = (p.getCategoria() != null) ? p.getCategoria().getNome() : "-";

                modeloTabela.addRow(new Object[]{
                        p.getId(),
                        p.getNome(),
                        nomeCat,
                        p.getPrecoVenda(),
                        p.getQuantidadeEstoque(),
                        nomeForn
                });
            }
        }
    }

    private void criarTabelaModerna() {
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
        tabela.getColumnModel().getColumn(3).setCellRenderer(center);

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

            // Aqui seria ideal o Controller ter um buscarPorId. Para ser rápido, pegamos da lista que já temos:
            Produto p = produtoController.buscarTodos().stream()
                    .filter(prod -> prod.getId().equals(id))
                    .findFirst().orElse(null);

            if(p != null) abrirFormulario(p);
        }
    }

    private void excluirSelecionado() {
        int linha = tabela.getSelectedRow();
        if(linha >= 0) {
            if(JOptionPane.showConfirmDialog(this, "Tem certeza? O produto será arquivado.", "Excluir", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                Integer id = (Integer) tabela.getValueAt(linha, 0);

                // O Controller faz a mágica de arquivar (setar falso e salvar)!
                produtoController.arquivar(id);
                carregarDados();
            }
        }
    }
}