package br.com.sualoja.view;

import br.com.sualoja.controller.UsuarioController;
import org.springframework.stereotype.Component;
import javax.swing.*;
import java.awt.*;

@Component
public class TelaCadastro extends JDialog {

    private final UsuarioController usuarioController;

    public TelaCadastro(UsuarioController usuarioController) {
        this.usuarioController = usuarioController;
    }

    public void iniciar() {
        setTitle("Novo Cadastro");
        setSize(400, 350);
        setModal(true); // Bloqueia a tela de trás
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 1, 10, 10));

        JTextField txtNome = new JTextField();
        JTextField txtLogin = new JTextField();
        JPasswordField txtSenha = new JPasswordField();
        JPasswordField txtConfSenha = new JPasswordField();

        add(criarCampo("Nome Completo:", txtNome));
        add(criarCampo("Login:", txtLogin));
        add(criarCampo("Senha:", txtSenha));
        add(criarCampo("Confirmar Senha:", txtConfSenha));

        JButton btnSalvar = new JButton("CADASTRAR");
        btnSalvar.setBackground(new Color(40, 167, 69));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btnSalvar.addActionListener(e -> {
            try {
                usuarioController.cadastrar(
                    txtNome.getText(), 
                    txtLogin.getText(), 
                    new String(txtSenha.getPassword()), 
                    new String(txtConfSenha.getPassword())
                );
                JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!");
                dispose(); // Fecha tela
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Atenção", JOptionPane.WARNING_MESSAGE);
            }
        });

        JPanel panelBtn = new JPanel();
        panelBtn.add(btnSalvar);
        add(panelBtn);

        setVisible(true);
    }

    private JPanel criarCampo(String label, JComponent campo) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        p.add(new JLabel(label), BorderLayout.NORTH);
        p.add(campo, BorderLayout.CENTER);
        return p;
    }
}
