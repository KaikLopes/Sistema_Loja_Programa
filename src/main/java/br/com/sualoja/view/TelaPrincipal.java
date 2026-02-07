package br.com.sualoja.view;

import br.com.sualoja.controller.ProdutoController;
import br.com.sualoja.model.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.List;

@Component
public class TelaPrincipal extends JFrame {

    @Autowired
    private ProdutoController controller;

    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private TelaDashboard dashboardOrigem; // Para saber pra onde voltar

    public TelaPrincipal() {
        setTitle("Gestão de Estoque");
        setSize(900, 600);
        // IMPORTANTE: Dispose fecha só a janela, Exit fecha o programa todo.
        // Vamos tratar isso no método iniciar.
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    // Recebe o dashboard para poder voltar pra ele
    public void iniciar(TelaDashboard dashboard) {
        this.dashboardOrigem = dashboard;
        
        // Se clicar no X da janela, volta pro dashboard em vez de matar o programa
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                voltarParaDashboard();
            }
        });

        getContentPane().removeAll(); // Limpa componentes antigos
        criarBarraFerramentas();
        criarTabela();
        carregarDados();
        setVisible(true);
    }

    private void voltarParaDashboard() {
        this.dispose();
        if (dashboardOrigem != null) {
            dashboardOrigem.setVisible(true); // Reabre o dashboard
        }
    }

    private void criarBarraFerramentas() {
        JToolBar barra = new JToolBar();
        barra.setFloatable(false); // Fixa a barra
        barra.setBackground(new Color(240, 240, 240));
        barra.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnVoltar = criarBotao("Voltar", new Color(108, 117, 125));
        JButton btnNovo = criarBotao("Novo Produto", new Color(40, 167, 69));
        JButton btnEditar = criarBotao("Editar", new Color(255, 193, 7));
        JButton btnExcluir = criarBotao("Excluir", new Color(220, 53, 69)); // Vermelho
        
        // Texto preto no botão amarelo para ler melhor
        btnEditar.setForeground(Color.BLACK); 

        // --- AÇÕES ---
        btnVoltar.addActionListener(e -> voltarParaDashboard());
        btnNovo.addActionListener(e -> abrirFormularioCadastro());
        
        btnEditar.addActionListener(e -> {
            int linha = tabela.getSelectedRow();
            if (linha >= 0) {
                Integer id = (Integer) tabela.getValueAt(linha, 0);
                String nome = (String) tabela.getValueAt(linha, 1);
                BigDecimal preco = (BigDecimal) tabela.getValueAt(linha, 2);
                Integer qtd = (Integer) tabela.getValueAt(linha, 3);
                abrirFormularioEdicao(id, nome, preco, qtd);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um produto!");
            }
        });

        // AÇÃO DE EXCLUIR
        btnExcluir.addActionListener(e -> {
            int linha = tabela.getSelectedRow();
            if (linha >= 0) {
                Integer id = (Integer) tabela.getValueAt(linha, 0);
                String nome = (String) tabela.getValueAt(linha, 1);
                
                int confirmacao = JOptionPane.showConfirmDialog(this, 
                    "Tem certeza que deseja excluir o produto: " + nome + "?", 
                    "Confirmar Exclusão", 
                    JOptionPane.YES_NO_OPTION);
                
                if (confirmacao == JOptionPane.YES_OPTION) {
                    controller.excluirProduto(id); // Chama controller
                    carregarDados(); // Atualiza tabela
                    JOptionPane.showMessageDialog(this, "Produto excluído!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um produto para excluir!");
            }
        });

        barra.add(btnVoltar);
        barra.add(Box.createHorizontalStrut(20)); // Espaço
        barra.add(btnNovo);
        barra.add(Box.createHorizontalStrut(10));
        barra.add(btnEditar);
        barra.add(Box.createHorizontalStrut(10));
        barra.add(btnExcluir);

        add(barra, BorderLayout.NORTH);
    }
    
    // Método auxiliar para criar botões bonitos
    private JButton criarBotao(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        return btn;
    }

    private void criarTabela() {
        String[] colunas = {"ID", "Nome", "Preço (R$)", "Estoque"};
        
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabela = new JTable(modeloTabela);
        tabela.setRowHeight(25); // Linhas mais altas
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Margem em volta da tabela
        add(scroll, BorderLayout.CENTER);
    }

    private void carregarDados() {
        modeloTabela.setRowCount(0);
        List<Produto> lista = controller.buscarTodos();
        for (Produto p : lista) {
            modeloTabela.addRow(new Object[]{
                p.getId(), p.getNome(), p.getPrecoVenda(), p.getQuantidadeEstoque()
            });
        }
    }

    // Formulários (Cadastro e Edição) mantidos iguais, só com pequenos ajustes visuais se quiser
    private void abrirFormularioCadastro() {
        JDialog dialog = new JDialog(this, "Novo Produto", true);
        configurarDialog(dialog); // Método auxiliar abaixo

        JTextField txtNome = new JTextField();
        JTextField txtPreco = new JTextField();
        JTextField txtQtd = new JTextField();

        dialog.add(new JLabel("Nome:")); dialog.add(txtNome);
        dialog.add(new JLabel("Preço:")); dialog.add(txtPreco);
        dialog.add(new JLabel("Qtd:")); dialog.add(txtQtd);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setBackground(new Color(40, 167, 69));
        btnSalvar.setForeground(Color.WHITE);
        
        btnSalvar.addActionListener(ev -> {
            try {
                controller.salvarProduto(txtNome.getText(), txtPreco.getText(), txtQtd.getText());
                dialog.dispose();
                carregarDados();
                JOptionPane.showMessageDialog(this, "Salvo!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erro: " + ex.getMessage());
            }
        });
        dialog.add(new JLabel("")); dialog.add(btnSalvar);
        dialog.setVisible(true);
    }

    private void abrirFormularioEdicao(Integer id, String nome, BigDecimal preco, Integer qtd) {
        JDialog dialog = new JDialog(this, "Editar Produto", true);
        configurarDialog(dialog);

        JTextField txtNome = new JTextField(nome);
        JTextField txtPreco = new JTextField(preco.toString());
        JTextField txtQtd = new JTextField(qtd.toString());

        dialog.add(new JLabel("Nome:")); dialog.add(txtNome);
        dialog.add(new JLabel("Preço:")); dialog.add(txtPreco);
        dialog.add(new JLabel("Qtd:")); dialog.add(txtQtd);

        JButton btnSalvar = new JButton("Atualizar");
        btnSalvar.setBackground(new Color(255, 193, 7));
        
        btnSalvar.addActionListener(ev -> {
            try {
                controller.atualizarProduto(id, txtNome.getText(), txtPreco.getText(), txtQtd.getText());
                dialog.dispose();
                carregarDados();
                JOptionPane.showMessageDialog(this, "Atualizado!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erro: " + ex.getMessage());
            }
        });
        dialog.add(new JLabel("")); dialog.add(btnSalvar);
        dialog.setVisible(true);
    }
    
    private void configurarDialog(JDialog dialog) {
        dialog.setSize(300, 250);
        dialog.setLayout(new GridLayout(4, 2, 10, 10));
        dialog.setLocationRelativeTo(this);
        ((JPanel)dialog.getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
}