package br.com.sualoja.view;

import br.com.sualoja.controller.ClienteController;
import br.com.sualoja.model.Cliente;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelClientes extends JPanel {

    private ClienteController clienteController; // <-- Só o Controller!
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public PanelClientes(ClienteController clienteController) {
        this.clienteController = clienteController;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 246, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 246, 250));
        JLabel lblTitulo = new JLabel("Gerenciar Clientes");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JButton btnNovo = new JButton("+ Novo Cliente");
        btnNovo.setBackground(new Color(0, 123, 255));
        btnNovo.setForeground(Color.WHITE);
        btnNovo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnNovo.setFocusPainted(false);
        btnNovo.addActionListener(e -> abrirFormulario(null));

        topPanel.add(lblTitulo, BorderLayout.WEST);
        topPanel.add(btnNovo, BorderLayout.EAST);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0,0,15,0));

        String[] colunas = {"ID", "Nome", "CPF", "Telefone", "Endereço"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tabela = new JTable(modeloTabela);
        configurarTabela();

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(245, 246, 250));

        JButton btnEditar = new JButton("Editar");
        btnEditar.setBackground(new Color(255, 193, 7));
        btnEditar.setFocusPainted(false);
        btnEditar.addActionListener(e -> editarSelecionado());

        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.setBackground(new Color(220, 53, 69));
        btnExcluir.setForeground(Color.WHITE);
        btnExcluir.setFocusPainted(false);
        btnExcluir.addActionListener(e -> excluirSelecionado());

        bottomPanel.add(btnEditar);
        bottomPanel.add(btnExcluir);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        carregarDados();
    }

    public void carregarDados() {
        modeloTabela.setRowCount(0);
        modeloTabela.addRow(new Object[]{"...", "Buscando clientes...", "", "", ""});

        SwingWorker<List<Cliente>, Void> worker = new SwingWorker<>() {
            @Override protected List<Cliente> doInBackground() {
                return clienteController.buscarTodos();
            }
            @Override protected void done() {
                try {
                    List<Cliente> lista = get();
                    modeloTabela.setRowCount(0);
                    for (Cliente c : lista) {
                        modeloTabela.addRow(new Object[]{c.getId(), c.getNome(), c.getCpf(), c.getTelefone(), c.getEndereco()});
                    }
                } catch (Exception e) {
                    modeloTabela.setRowCount(0);
                }
            }
        };
        worker.execute();
    }

    private void abrirFormulario(Cliente cliente) {
        boolean edicao = (cliente != null);
        JDialog d = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), edicao ? "Editar" : "Novo Cliente", true);
        d.setSize(450, 400);
        d.setLayout(new GridLayout(5, 2, 10, 10));
        d.setLocationRelativeTo(this);

        JTextField txtNome = new JTextField(edicao ? cliente.getNome() : "");
        JTextField txtCpf = new JTextField(edicao ? cliente.getCpf() : "");
        JTextField txtTel = new JTextField(edicao ? cliente.getTelefone() : "");
        JTextField txtEnd = new JTextField(edicao ? cliente.getEndereco() : "");

        d.add(new JLabel(" Nome:")); d.add(txtNome);
        d.add(new JLabel(" CPF:")); d.add(txtCpf);
        d.add(new JLabel(" Telefone:")); d.add(txtTel);
        d.add(new JLabel(" Endereço:")); d.add(txtEnd);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setBackground(new Color(40, 167, 69));
        btnSalvar.setForeground(Color.WHITE);

        btnSalvar.addActionListener(e -> {
            try {
                Cliente c = edicao ? cliente : new Cliente();
                c.setNome(txtNome.getText());
                c.setCpf(txtCpf.getText());
                c.setTelefone(txtTel.getText());
                c.setEndereco(txtEnd.getText());

                clienteController.salvar(c); // <-- Validação no backend!
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
        if(row >= 0) {
            Long id = (Long) tabela.getValueAt(row, 0);
            Cliente c = clienteController.buscarPorId(id);
            if(c != null) abrirFormulario(c);
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um cliente para editar.");
        }
    }

    private void excluirSelecionado() {
        int row = tabela.getSelectedRow();
        if(row >= 0) {
            if(JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                try {
                    clienteController.excluir((Long) tabela.getValueAt(row, 0));
                    carregarDados();
                } catch(Exception e) {
                    JOptionPane.showMessageDialog(this, "Não é possível excluir cliente com vendas vinculadas!");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um cliente para excluir.");
        }
    }

    private void configurarTabela() {
        tabela.setRowHeight(30);
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabela.getTableHeader().setBackground(Color.WHITE);
        tabela.setShowVerticalLines(false);
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        tabela.getColumnModel().getColumn(0).setCellRenderer(center);
        tabela.getColumnModel().getColumn(2).setCellRenderer(center);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(200);
        tabela.getColumnModel().getColumn(4).setPreferredWidth(250);
    }
}