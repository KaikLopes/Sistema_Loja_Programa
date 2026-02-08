package br.com.sualoja.view;

import br.com.sualoja.controller.UsuarioController;
import br.com.sualoja.model.Usuario;
import javax.swing.*;
import java.awt.*;

public class PanelPerfil extends JPanel {

    private final Usuario usuarioLogado;
    private final UsuarioController usuarioController;
    private final JLabel lblBoasVindasSidebar;

    public PanelPerfil(Usuario user, UsuarioController controller, JLabel lblSidebar) {
        this.usuarioLogado = user;
        this.usuarioController = controller;
        this.lblBoasVindasSidebar = lblSidebar;

        setLayout(new BorderLayout());
        setBackground(new Color(245, 246, 250)); // Fundo cinza claro
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Título do Painel
        JLabel lblTitulo = new JLabel("Meu Perfil");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(33, 41, 54));

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(245, 246, 250));
        headerPanel.add(lblTitulo);

        // --- PAINEL DO FORMULÁRIO (ESTILO CARTÃO) ---
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        // Campos
        JTextField txtNome = criarTextField(usuarioLogado.getNome());
        JTextField txtLogin = criarTextField(usuarioLogado.getLogin());
        JPasswordField txtSenha = new JPasswordField();
        estilizarCampo(txtSenha);

        // Adicionando ao cartão com espaçamentos
        cardPanel.add(criarLabel("Nome Completo:"));
        cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        cardPanel.add(txtNome);

        cardPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Espaço

        cardPanel.add(criarLabel("Login de Acesso:"));
        cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        cardPanel.add(txtLogin);

        cardPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Espaço

        cardPanel.add(criarLabel("Nova Senha (deixe vazio para manter):"));
        cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        cardPanel.add(txtSenha);

        cardPanel.add(Box.createRigidArea(new Dimension(0, 30))); // Espaço para o botão

        // Botão Salvar
        JButton btnSalvar = new JButton("SALVAR ALTERAÇÕES");
        btnSalvar.setBackground(new Color(0, 123, 255));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSalvar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45)); // Ocupa largura total do card
        btnSalvar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalvar.setFocusPainted(false);

        btnSalvar.addActionListener(e -> {
            try {
                String novaSenha = new String(txtSenha.getPassword());
                usuarioController.atualizarDados(usuarioLogado, txtNome.getText(), txtLogin.getText(), novaSenha);

                JOptionPane.showMessageDialog(this, "Perfil atualizado com sucesso!");

                if(lblBoasVindasSidebar != null) {
                    String primeiroNome = txtNome.getText().split(" ")[0];
                    lblBoasVindasSidebar.setText("Olá, " + primeiroNome);
                }
                txtSenha.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        cardPanel.add(btnSalvar);

        // Wrapper para centralizar o cartão na tela e limitar a largura
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(new Color(245, 246, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 1; gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Estica horizontalmente
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(0, 50, 0, 50); // Margens laterais grandes para centralizar visualmente

        // Define um tamanho máximo preferido para o formulário não ficar gigante em telas grandes
        cardPanel.setPreferredSize(new Dimension(500, 400));

        centerWrapper.add(cardPanel, gbc);

        // Adiciona tudo ao painel principal
        add(headerPanel, BorderLayout.NORTH);
        add(centerWrapper, BorderLayout.CENTER);
    }

    private JLabel criarLabel(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("Segoe UI", Font.BOLD, 14));
        l.setForeground(new Color(80, 80, 80));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JTextField criarTextField(String valor) {
        JTextField tf = new JTextField(valor);
        estilizarCampo(tf);
        return tf;
    }

    private void estilizarCampo(JComponent c) {
        c.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        c.setPreferredSize(new Dimension(0, 35));
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        if(c instanceof JComponent) ((JComponent)c).setAlignmentX(Component.LEFT_ALIGNMENT);
    }
}