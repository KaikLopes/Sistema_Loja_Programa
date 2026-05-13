package br.com.sualoja.view;

import br.com.sualoja.controller.UsuarioController;
import br.com.sualoja.model.Usuario;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Component
public class TelaLogin extends JFrame {

    private final UsuarioController usuarioController;

    private final TelaPrincipal telaPrincipal;

    private final TelaCadastro telaCadastro;

    public TelaLogin(UsuarioController usuarioController, TelaPrincipal telaPrincipal, TelaCadastro telaCadastro) {
        this.usuarioController = usuarioController;
        this.telaPrincipal = telaPrincipal;
        this.telaCadastro = telaCadastro;
    }

    public void iniciar() {
        setTitle("Login - Sistema Loja");
        setSize(400, 450); // Aumentei um pouco a altura para caber tudo bem
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        panel.setBackground(Color.WHITE);

        // Componentes
        JLabel lblTitulo = new JLabel("Bem-vindo");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        
        JLabel lblUser = new JLabel("Usuário");
        lblUser.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        JTextField txtLogin = new JTextField();
        txtLogin.setMaximumSize(new Dimension(300, 30));
        
        JLabel lblPass = new JLabel("Senha");
        lblPass.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        JPasswordField txtSenha = new JPasswordField();
        txtSenha.setMaximumSize(new Dimension(300, 30));

        JButton btnEntrar = new JButton("ENTRAR");
        btnEntrar.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        btnEntrar.setBackground(new Color(33, 41, 54)); 
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setMaximumSize(new Dimension(300, 40));
        btnEntrar.setFocusPainted(false);
        btnEntrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel lblCadastrar = new JLabel("Criar nova conta");
        lblCadastrar.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
        lblCadastrar.setForeground(new Color(0, 123, 255));
        lblCadastrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // --- AÇÃO DO LOGIN ---
        btnEntrar.addActionListener(e -> {
            try {
                // Valida usuário
                String senha = new String(txtSenha.getPassword());
                Usuario usuario = usuarioController.autenticar(txtLogin.getText(), senha);
                
                if (usuario != null) {
                    this.dispose(); // Fecha login
                    // CORREÇÃO AQUI: Passamos o objeto 'usuario' para a tela principal
                    telaPrincipal.iniciar(usuario); 
                } else {
                    JOptionPane.showMessageDialog(this, "Login ou senha inválidos!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (Exception ex) {
                // Fallback para testes (admin/admin)
                if(txtLogin.getText().equals("admin") && new String(txtSenha.getPassword()).equals("admin")) {
                    Usuario u = new Usuario(); u.setNome("Administrador");
                    this.dispose();
                    telaPrincipal.iniciar(u); // Passa o usuário admin
                } else {
                    JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
                }
            }
        });

        lblCadastrar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                telaCadastro.iniciar();
            }
        });

        // Adiciona tudo ao painel
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
