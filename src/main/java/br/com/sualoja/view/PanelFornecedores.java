package br.com.sualoja.view;

import br.com.sualoja.controller.FornecedorController;
import br.com.sualoja.controller.ProdutoController;
import br.com.sualoja.model.Fornecedor;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelFornecedores extends JPanel {

    private FornecedorController fornecedorController;
    private ProdutoController produtoController;
    private JTable tabela;
    private DefaultTableModel modelo;

    public PanelFornecedores(FornecedorController fController, ProdutoController pController) {
        this.fornecedorController = fController;
        this.produtoController = pController;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 246, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(245, 246, 250));
        JLabel lbl = new JLabel("Fornecedores");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JButton btnNovo = new JButton("+ Novo Fornecedor");
        btnNovo.setBackground(new Color(0, 123, 255));
        btnNovo.setForeground(Color.WHITE);
        btnNovo.addActionListener(e -> abrirFormulario(null));

        top.add(lbl, BorderLayout.WEST);
        top.add(btnNovo, BorderLayout.EAST);
        top.setBorder(BorderFactory.createEmptyBorder(0,0,15,0));

        String[] cols = {"ID", "Nome Fantasia", "Razão Social", "CNPJ"};
        modelo = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(modelo);
        tabela.setRowHeight(30);
        tabela.getTableHeader().setBackground(Color.WHITE);

        tabela.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = tabela.getSelectedRow();
                    if(row != -1) {
                        Long id = (Long) tabela.getValueAt(row, 0);
                        mostrarProdutosDoFornecedor(id);
                    }
                }
            }
        });

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

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        carregarDados();
    }

    private void mostrarProdutosDoFornecedor(Long idFornecedor) {
        String resumo = produtoController.obterResumoProdutosDoFornecedor(idFornecedor);
        JOptionPane.showMessageDialog(this, new JScrollPane(new JTextArea(resumo, 10, 30)));
    }

    public void carregarDados() {
        modelo.setRowCount(0);
        modelo.addRow(new Object[]{"...", "Buscando fornecedores...", "", ""});

        SwingWorker<List<Fornecedor>, Void> worker = new SwingWorker<>() {
            @Override protected List<Fornecedor> doInBackground() {
                return fornecedorController.buscarTodos();
            }
            @Override protected void done() {
                try {
                    List<Fornecedor> lista = get();
                    modelo.setRowCount(0);
                    for(Fornecedor f : lista) {
                        modelo.addRow(new Object[]{f.getId(), f.getNomeFantasia(), f.getRazaoSocial(), f.getCnpj()});
                    }
                } catch (Exception e) {
                    modelo.setRowCount(0);
                }
            }
        };
        worker.execute();
    }

    private void abrirFormulario(Fornecedor f) {
        boolean edicao = (f != null);
        JDialog d = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), edicao ? "Editar" : "Novo", true);
        d.setSize(400, 300);
        d.setLayout(new GridLayout(4, 2, 10, 10));
        d.setLocationRelativeTo(this);

        JTextField txtFantasia = new JTextField(edicao ? f.getNomeFantasia() : "");
        JTextField txtRazao = new JTextField(edicao ? f.getRazaoSocial() : "");
        JTextField txtCnpj = new JTextField(edicao ? f.getCnpj() : "");

        d.add(new JLabel(" Nome Fantasia:")); d.add(txtFantasia);
        d.add(new JLabel(" Razão Social:")); d.add(txtRazao);
        d.add(new JLabel(" CNPJ:")); d.add(txtCnpj);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setBackground(new Color(40, 167, 69));
        btnSalvar.setForeground(Color.WHITE);

        btnSalvar.addActionListener(e -> {
            try {
                Fornecedor novo = edicao ? f : new Fornecedor();
                novo.setNomeFantasia(txtFantasia.getText());
                novo.setRazaoSocial(txtRazao.getText());
                novo.setCnpj(txtCnpj.getText());

                fornecedorController.salvar(novo);
                d.dispose();
                carregarDados();
                JOptionPane.showMessageDialog(this, "Salvo com sucesso!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(d, "Erro: " + ex.getMessage());
            }
        });

        d.add(new JLabel("")); d.add(btnSalvar);
        d.setVisible(true);
    }

    private void editarSelecionado() {
        int row = tabela.getSelectedRow();
        if(row != -1) {
            Long id = (Long) tabela.getValueAt(row, 0);
            Fornecedor f = fornecedorController.buscarTodos().stream()
                    .filter(forn -> forn.getId().equals(id))
                    .findFirst().orElse(null);
            if(f != null) abrirFormulario(f);
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um fornecedor.");
        }
    }

    private void excluirSelecionado() {
        int row = tabela.getSelectedRow();
        if(row != -1) {
            if(JOptionPane.showConfirmDialog(this, "Tem certeza?", "Excluir", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                try {
                    fornecedorController.excluir((Long) tabela.getValueAt(row, 0));
                    carregarDados();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir (pode ter produtos vinculados).");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um fornecedor.");
        }
    }
}