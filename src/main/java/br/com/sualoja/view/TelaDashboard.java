package br.com.sualoja.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.swing.*;
import java.awt.*;

@Component
public class TelaDashboard extends JFrame {

    @Autowired
    private TelaPrincipal telaProdutos;

    public TelaDashboard() {
        setTitle("Dashboard - Sistema Loja");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Fechar aqui mata o programa (Correto)
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    public void iniciar(String nomeUsuario) {
        getContentPane().removeAll(); // Limpa tela para não duplicar se chamar de novo

        // --- HEADER (Topo) ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(33, 37, 41)); // Cinza Escuro Profissional
        header.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel lblTitulo = new JLabel("Olá, " + nomeUsuario);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        
        JButton btnSair = new JButton("Sair do Sistema");
        btnSair.setBackground(new Color(220, 53, 69)); // Vermelho
        btnSair.setForeground(Color.WHITE);
        btnSair.setFocusPainted(false);
        btnSair.addActionListener(e -> System.exit(0));
        
        header.add(lblTitulo, BorderLayout.WEST);
        header.add(btnSair, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // --- DASHBOARD (Grid de Cartões) ---
        JPanel painelCentral = new JPanel(new GridLayout(2, 2, 30, 30));
        painelCentral.setBackground(new Color(248, 249, 250)); // Fundo claro
        painelCentral.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Botão Produtos (Verde)
        painelCentral.add(criarCard("PRODUTOS", "Gerenciar Estoque", new Color(40, 167, 69), e -> {
            this.setVisible(false); // Esconde o dashboard
            telaProdutos.iniciar(this); // Passa o Dashboard para a tela de produtos saber voltar
        }));

        // Botão Vendas (Amarelo)
        painelCentral.add(criarCard("VENDAS", "Nova Venda", new Color(255, 193, 7), e -> {
            JOptionPane.showMessageDialog(this, "Módulo de Vendas (Em breve)");
        }));

        // Botão Clientes (Azul)
        painelCentral.add(criarCard("CLIENTES", "Base de Clientes", new Color(23, 162, 184), e -> {
            JOptionPane.showMessageDialog(this, "Módulo de Clientes (Em breve)");
        }));
        
        // Botão Financeiro (Cinza)
        painelCentral.add(criarCard("RELATÓRIOS", "Ver Faturamento", new Color(108, 117, 125), e -> {
            JOptionPane.showMessageDialog(this, "Relatórios (Em breve)");
        }));

        add(painelCentral, BorderLayout.CENTER);
        
        revalidate();
        repaint();
        setVisible(true);
    }

    private JPanel criarCard(String titulo, String subtitulo, Color cor, java.awt.event.ActionListener acao) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        // Borda colorida grossa na esquerda
        card.setBorder(BorderFactory.createMatteBorder(0, 10, 0, 0, cor));
        
        JPanel textoPanel = new JPanel(new GridLayout(2, 1));
        textoPanel.setBackground(Color.WHITE);
        textoPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(cor);

        JLabel lblSub = new JLabel(subtitulo);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(Color.GRAY);

        textoPanel.add(lblTitulo);
        textoPanel.add(lblSub);

        JButton btn = new JButton("ACESSAR");
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE); // Texto preto ou branco dependendo da cor
        if(cor.equals(new Color(255, 193, 7))) btn.setForeground(Color.BLACK); // Ajuste pro amarelo
        
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.addActionListener(acao);
        
        // Cursor de mãozinha
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        card.add(textoPanel, BorderLayout.CENTER);
        card.add(btn, BorderLayout.SOUTH);
        
        // Sombra fake (borda cinza leve em volta)
        JPanel shadow = new JPanel(new BorderLayout());
        shadow.setBorder(BorderFactory.createLineBorder(new Color(220,220,220), 1));
        shadow.add(card, BorderLayout.CENTER);
        
        return shadow;
    }
}