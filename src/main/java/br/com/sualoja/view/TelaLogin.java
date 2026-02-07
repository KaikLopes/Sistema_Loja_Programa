package br.com.sualoja.view;

import br.com.sualoja.controller.UsuarioController;
import br.com.sualoja.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component; // A Culpa é dessa anotação aqui (conflito de nome)

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Component
public class TelaLogin extends JFrame {

    @Autowired
    private UsuarioController usuarioController;

    @Autowired
    private TelaDashboard telaDashboard;

    @Autowired
    private TelaCadastro telaCadastro;

    public TelaLogin() {
        setTitle("Login - Sistema Loja");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
    }

    public void iniciar() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        panel.setBackground(Color.WHITE);

        // Título
        JLabel lblTitulo = new JLabel("Bem-vindo");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        // CORREÇÃO AQUI: Usando java.awt.Component explicitamente
        lblTitulo.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        
        // Campos
        JLabel lblUser = new JLabel("Usuário");
        lblUser.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT); // CORREÇÃO
        JTextField txtLogin = new JTextField();
        txtLogin.setMaximumSize(new Dimension(300, 30));
        
        JLabel lblPass = new JLabel("Senha");
        lblPass.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT); // CORREÇÃO
        JPasswordField txtSenha = new JPasswordField();
        txtSenha.setMaximumSize(new Dimension(300, 30));

        // Botão Entrar
        JButton btnEntrar = new JButton("ENTRAR NO SISTEMA");
        btnEntrar.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT); // CORREÇÃO
        btnEntrar.setBackground(new Color(51, 102, 255));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEntrar.setMaximumSize(new Dimension(300, 40));
        btnEntrar.setFocusPainted(false);
        
        // Link de Cadastro
        JLabel lblCadastrar = new JLabel("Não tem conta? Cadastre-se aqui.");
        lblCadastrar.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT); // CORREÇÃO
        lblCadastrar.setForeground(new Color(51, 102, 255));
        lblCadastrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // --- AÇÕES ---
        btnEntrar.addActionListener(e -> {
            try {
                String login = txtLogin.getText();
                String senha = new String(txtSenha.getPassword());
                
                Usuario usuarioLogado = usuarioController.autenticar(login, senha);
                
                this.dispose();
                telaDashboard.iniciar(usuarioLogado.getNome());
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Login", JOptionPane.ERROR_MESSAGE);
            }
        });

        lblCadastrar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                telaCadastro.iniciar();
            }
        });

        // Adicionando componentes
        panel.add(lblTitulo);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(lblUser);
        panel.add(txtLogin);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(lblPass);
        panel.add(txtSenha);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(btnEntrar);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(lblCadastrar);

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }
}