package br.com.sualoja.view;

import br.com.sualoja.dao.FornecedorDAO;
import br.com.sualoja.dao.ProdutoDAO;
import br.com.sualoja.model.Fornecedor;
import br.com.sualoja.model.Produto;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelFornecedores extends JPanel {

    private FornecedorDAO fornecedorDAO;
    private ProdutoDAO produtoDAO;
    private JTable tabela;
    private DefaultTableModel modelo;

    public PanelFornecedores(FornecedorDAO fDao, ProdutoDAO pDao) {
        this.fornecedorDAO = fDao;
        this.produtoDAO = pDao;
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
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
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
        List<Produto> produtos = produtoDAO.findByFornecedorId(idFornecedor);
        StringBuilder sb = new StringBuilder("Produtos deste fornecedor:\n\n");
        if(produtos.isEmpty()) sb.append("Nenhum produto cadastrado para este fornecedor.");
        for(Produto p : produtos) {
            if(p.getAtivo()) {
                sb.append("- ").append(p.getNome()).append(" (Estoque: ").append(p.getQuantidadeEstoque()).append(")\n");
            }
        }
        JOptionPane.showMessageDialog(this, new JScrollPane(new JTextArea(sb.toString(), 10, 30)));
    }

    public void carregarDados() {
        modelo.setRowCount(0);
        // MÉTODO ATUALIZADO
        for(Fornecedor f : fornecedorDAO.findAllByOrderByNomeFantasiaAsc()) {
            modelo.addRow(new Object[]{f.getId(), f.getNomeFantasia(), f.getRazaoSocial(), f.getCnpj()});
        }
    }

    private void abrirFormulario(Fornecedor f) {
        boolean edicao = (f != null);
        JDialog d = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), edicao ? "Editar Fornecedor" : "Novo Fornecedor", true);
        d.setSize(400, 300);
        d.setLayout(new GridLayout(4, 2, 10, 10));
        d.setLocationRelativeTo(this);

        JTextField txtFantasia = new JTextField(edicao ? f.getNomeFantasia() : "");
        JTextField txtRazao = new JTextField(edicao ? f.getRazaoSocial() : "");
        JTextField txtCnpj = new JTextField(edicao ? f.getCnpj() : "");
        txtCnpj.setToolTipText("Digite apenas números (14 dígitos)");

        d.add(new JLabel(" Nome Fantasia:")); d.add(txtFantasia);
        d.add(new JLabel(" Razão Social:")); d.add(txtRazao);
        d.add(new JLabel(" CNPJ:")); d.add(txtCnpj);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setBackground(new Color(40, 167, 69));
        btnSalvar.setForeground(Color.WHITE);
        
        btnSalvar.addActionListener(e -> {
            try {
                String cnpjLimpo = txtCnpj.getText().replaceAll("[^0-9]", "");
                
                if (txtFantasia.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(d, "O Nome Fantasia é obrigatório.");
                    return;
                }

                if (cnpjLimpo.length() != 14) {
                    JOptionPane.showMessageDialog(d, "CNPJ Inválido! Deve ter exatamente 14 números.");
                    return;
                }

                Fornecedor novo = edicao ? f : new Fornecedor();
                novo.setNomeFantasia(txtFantasia.getText());
                novo.setRazaoSocial(txtRazao.getText());
                novo.setCnpj(txtCnpj.getText());
                
                fornecedorDAO.save(novo);
                d.dispose();
                carregarDados();
                JOptionPane.showMessageDialog(this, "Salvo com sucesso!");
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(d, "Erro ao salvar: " + ex.getMessage());
            }
        });

        d.add(new JLabel("")); d.add(btnSalvar);
        d.setVisible(true);
    }
    
    private void editarSelecionado() {
        int row = tabela.getSelectedRow();
        if(row != -1) {
            Long id = (Long) tabela.getValueAt(row, 0);
            Fornecedor f = fornecedorDAO.findById(id).orElse(null);
            if(f != null) abrirFormulario(f);
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um fornecedor.");
        }
    }
    
    private void excluirSelecionado() {
        int row = tabela.getSelectedRow();
        if(row != -1) {
            if(JOptionPane.showConfirmDialog(this, "Tem certeza?", "Excluir", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                Long id = (Long) tabela.getValueAt(row, 0);
                try {
                    fornecedorDAO.deleteById(id);
                    carregarDados();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Não é possível excluir (pode ter produtos vinculados).");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um fornecedor.");
        }
    }
}