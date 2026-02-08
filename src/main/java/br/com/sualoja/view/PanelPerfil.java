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
        // Borda suave
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        // Campos
        JTextField txtNome = criarTextField(usuarioLogado.getNome());
        JTextField txtLogin = criarTextField(usuarioLogado.getLogin());
        txtLogin.setEditable(false); // Login geralmente não se muda, mas se quiser pode deixar true
        
        JPasswordField txtSenha = new JPasswordField();
        estilizarCampo(txtSenha);

        // Adicionando ao cartão com espaçamentos
        cardPanel.add(criarLabel("Nome Completo:"));
        cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        cardPanel.add(txtNome);

        cardPanel.add(Box.createRigidArea(new Dimension(0, 20))); 

        cardPanel.add(criarLabel("Login de Acesso:"));
        cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        cardPanel.add(txtLogin);

        cardPanel.add(Box.createRigidArea(new Dimension(0, 20))); 

        cardPanel.add(criarLabel("Nova Senha (deixe vazio para manter):"));
        cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        cardPanel.add(txtSenha);

        cardPanel.add(Box.createRigidArea(new Dimension(0, 30))); 

        // Botão Salvar
        JButton btnSalvar = new JButton("SALVAR ALTERAÇÕES");
        btnSalvar.setBackground(new Color(0, 123, 255));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSalvar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45)); 
        btnSalvar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalvar.setFocusPainted(false);
        btnSalvar.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnSalvar.addActionListener(e -> {
            try {
                String novaSenha = new String(txtSenha.getPassword());
                
                // Atualiza objeto localmente
                usuarioLogado.setNome(txtNome.getText());
                if (!novaSenha.isEmpty()) {
                    usuarioLogado.setSenha(novaSenha);
                }

                // Tenta salvar no banco via controller
                // Se seu controller não tiver o método 'atualizarDados', você pode criar ou usar 'salvar'
                // usuarioController.atualizarDados(usuarioLogado, txtNome.getText(), txtLogin.getText(), novaSenha);
                
                // Exemplo genérico de salvamento (descomente se tiver o método salvar)
                // usuarioController.salvar(usuarioLogado);

                JOptionPane.showMessageDialog(this, "Perfil atualizado com sucesso!");

                // Atualiza o nome na barra lateral
                if(lblBoasVindasSidebar != null) {
                    String primeiroNome = txtNome.getText().split(" ")[0];
                    lblBoasVindasSidebar.setText("Olá, " + primeiroNome);
                }
                txtSenha.setText("");
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        cardPanel.add(btnSalvar);

        // Wrapper para centralizar o cartão na tela
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(new Color(245, 246, 250));
        
        // Configuração para o card não esticar demais
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; 
        gbc.gridy = 0;
        gbc.weightx = 1; 
        gbc.weighty = 0; 
        gbc.anchor = GridBagConstraints.NORTH; 
        gbc.insets = new Insets(20, 0, 0, 0); 

        // Tamanho fixo confortável para o formulário
        cardPanel.setPreferredSize(new Dimension(500, 400));
        cardPanel.setMaximumSize(new Dimension(500, 450));

        centerWrapper.add(cardPanel, gbc);

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
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        c.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), 
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }
}