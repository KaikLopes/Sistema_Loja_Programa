package br.com.sualoja.view;

import br.com.sualoja.controller.CategoriaController;
import br.com.sualoja.controller.ProdutoController;
import br.com.sualoja.model.Categoria;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelCategorias extends JPanel {

    private CategoriaController categoriaController;
    private ProdutoController produtoController;
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public PanelCategorias(CategoriaController categoriaController, ProdutoController produtoController) {
        this.categoriaController = categoriaController;
        this.produtoController = produtoController;

        setLayout(new BorderLayout());
        setBackground(new Color(245, 246, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- TOPO ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 246, 250));
        JLabel lblTitulo = new JLabel("Gerenciar Categorias");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JButton btnNovo = new JButton("+ Nova Categoria");
        btnNovo.setBackground(new Color(0, 123, 255));
        btnNovo.setForeground(Color.WHITE);
        btnNovo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnNovo.setFocusPainted(false);
        btnNovo.addActionListener(e -> abrirFormulario(null));

        topPanel.add(lblTitulo, BorderLayout.WEST);
        topPanel.add(btnNovo, BorderLayout.EAST);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0,0,15,0));

        // --- TABELA ---
        String[] colunas = {"ID", "Nome da Categoria"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tabela = new JTable(modeloTabela);
        tabela.setRowHeight(30);
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabela.getTableHeader().setBackground(Color.WHITE);

        // --- EVENTO DE CLIQUE DUPLO ---
        tabela.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = tabela.getSelectedRow();
                    if(row != -1) {
                        Integer id = (Integer) tabela.getValueAt(row, 0);
                        mostrarProdutosDaCategoria(id);
                    }
                }
            }
        });

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        tabela.getColumnModel().getColumn(0).setCellRenderer(center);
        tabela.getColumnModel().getColumn(0).setMaxWidth(100);

        // --- BOTÕES RODAPÉ ---
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

    // --- LÓGICA TOTALMENTE LIMPA (SEM DAOS) ---
    private void mostrarProdutosDaCategoria(Integer idCategoria) {
        // Pede para o Controller o texto formatado!
        String resumo = produtoController.obterResumoProdutosDaCategoria(idCategoria);
        JOptionPane.showMessageDialog(this, new JScrollPane(new JTextArea(resumo, 10, 30)));
    }

    // --- CARREGAMENTO ASSÍNCRONO (SWING WORKER) ---
    public void carregarDados() {
        modeloTabela.setRowCount(0);
        modeloTabela.addRow(new Object[]{"...", "Carregando dados..."});

        SwingWorker<List<Categoria>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Categoria> doInBackground() throws Exception {
                // Vai no banco de dados EM SEGUNDO PLANO
                return categoriaController.listarTodas();
            }

            @Override
            protected void done() {
                try {
                    // Pega o resultado do banco
                    List<Categoria> lista = get();
                    modeloTabela.setRowCount(0);

                    for (Categoria c : lista) {
                        modeloTabela.addRow(new Object[]{c.getId(), c.getNome()});
                    }
                } catch (Exception e) {
                    modeloTabela.setRowCount(0);
                    JOptionPane.showMessageDialog(PanelCategorias.this, "Erro ao carregar categorias.");
                }
            }
        };
        worker.execute();
    }

    private void abrirFormulario(Categoria categoria) {
        boolean edicao = (categoria != null);
        JDialog d = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), edicao ? "Editar" : "Nova Categoria", true);
        d.setSize(400, 200);
        d.setLayout(new GridLayout(3, 2, 10, 10));
        d.setLocationRelativeTo(this);

        JTextField txtNome = new JTextField(edicao ? categoria.getNome() : "");

        d.add(new JLabel(" Nome da Categoria:")); d.add(txtNome);
        d.add(new JLabel(""));

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setBackground(new Color(40, 167, 69));
        btnSalvar.setForeground(Color.WHITE);

        btnSalvar.addActionListener(e -> {
            if (txtNome.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(d, "O nome é obrigatório!");
                return;
            }
            try {
                Categoria c = edicao ? categoria : new Categoria();
                c.setNome(txtNome.getText());

                // Usando o nome correto da variável: categoriaController
                categoriaController.salvar(c);

                d.dispose();
                carregarDados(); // Como agora é assíncrono, a tela vai mostrar "Carregando..." rapidinho e atualizar
                JOptionPane.showMessageDialog(this, "Salvo com sucesso!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(d, "Erro: " + ex.getMessage());
            }
        });

        d.add(btnSalvar);
        d.setVisible(true);
    }

    private void editarSelecionado() {
        int row = tabela.getSelectedRow();
        if(row >= 0) {
            Integer id = (Integer) tabela.getValueAt(row, 0);
            String nome = (String) tabela.getValueAt(row, 1);
            Categoria c = new Categoria(nome);
            c.setId(id);
            abrirFormulario(c);
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma categoria para editar.");
        }
    }

    private void excluirSelecionado() {
        int row = tabela.getSelectedRow();
        if(row >= 0) {
            if(JOptionPane.showConfirmDialog(this, "Tem certeza? Isso pode afetar produtos vinculados.", "Excluir", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                try {
                    Integer id = (Integer) tabela.getValueAt(row, 0);

                    // Usando o nome correto da variável: categoriaController
                    categoriaController.excluir(id);
                    carregarDados();
                } catch(Exception e) {
                    JOptionPane.showMessageDialog(this, "Não é possível excluir categoria em uso!");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma categoria para excluir.");
        }
    }
}