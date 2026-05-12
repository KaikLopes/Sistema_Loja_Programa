package br.com.sualoja.view;

import br.com.sualoja.controller.*;
import br.com.sualoja.dao.*;
import br.com.sualoja.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class TelaPrincipal extends JFrame {

    @Autowired private ProdutoController produtoController;
    @Autowired private VendaController vendaController;
    @Autowired private CategoriaController categoriaController;
    @Autowired private UsuarioController usuarioController;
    @Autowired private ClienteController clienteController;
    @Autowired private FornecedorController fornecedorController;
    @Autowired private RelatorioController relatorioController;
    @Autowired private DashboardController dashboardController;

    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JPanel sidebar;
    private boolean menuAberto = true;
    private List<JButton> botoesMenu = new ArrayList<>();
    private JLabel lblUsuario;
    private JButton botaoSelecionado;

    // Cores (Dark Blue Theme)
    private final Color SIDEBAR_BG = new Color(33, 41, 54);
    private final Color SIDEBAR_BTN_HOVER = new Color(50, 60, 75);
    private final Color SIDEBAR_BTN_SELECTED = new Color(0, 123, 255);
    private final Color TEXT_COLOR = new Color(220, 220, 220);

    public TelaPrincipal() {
        setTitle("Sua Loja - Gestão Profissional");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    public void iniciar(Usuario usuarioLogado) {
        getContentPane().removeAll(); // Limpa tela ao relogar

        // --- SIDEBAR ---
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(240, 0)); 

        // Header (Botão Menu)
        JPanel menuHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 25));
        menuHeader.setBackground(SIDEBAR_BG);
        menuHeader.setMaximumSize(new Dimension(240, 80));
        // CORREÇÃO 1: java.awt.Component explícito
        menuHeader.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

        JButton btnToggle = new JButton();
        btnToggle.setIcon(new IconeMenu(24, Color.WHITE)); 
        btnToggle.setContentAreaFilled(false);
        btnToggle.setBorderPainted(false);
        btnToggle.setFocusPainted(false);
        btnToggle.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnToggle.addActionListener(e -> alternarMenu());

        menuHeader.add(btnToggle);
        sidebar.add(menuHeader);
        
        // Label Usuário
        String primeiroNome = (usuarioLogado.getNome() != null) ? usuarioLogado.getNome().split(" ")[0] : "Usuário";
        lblUsuario = new JLabel("Olá, " + primeiroNome);
        lblUsuario.setForeground(Color.WHITE);
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblUsuario.setBorder(BorderFactory.createEmptyBorder(0, 25, 30, 0));
        // CORREÇÃO 2: java.awt.Component explícito
        lblUsuario.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        sidebar.add(lblUsuario);

        // --- BOTÕES DO MENU ---
        JButton btnDash = adicionarBotaoMenu(sidebar, "Dashboard", "dashboard");
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        
        adicionarBotaoMenu(sidebar, "Vendas", "vendas");
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));

        adicionarBotaoMenu(sidebar, "Produtos", "produtos");
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        
        adicionarBotaoMenu(sidebar, "Categorias", "categorias");
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        
        adicionarBotaoMenu(sidebar, "Clientes", "clientes");
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        
        adicionarBotaoMenu(sidebar, "Fornecedores", "fornecedores");
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        
        adicionarBotaoMenu(sidebar, "Relatórios", "relatorios");

        sidebar.add(Box.createVerticalGlue());
        
        // Botão Meu Perfil
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        adicionarBotaoMenu(sidebar, "Meu Perfil", "perfil");
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JButton btnSair = adicionarBotaoMenu(sidebar, "Sair do Sistema", "sair");
        btnSair.setForeground(new Color(255, 100, 100)); // Vermelho claro

        // --- CONTEÚDO PRINCIPAL ---
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        contentPanel.add(new PanelDashboard(dashboardController), "dashboard"); // Este limparemos na fase final
        contentPanel.add(new PanelProdutos(produtoController), "produtos");
        contentPanel.add(new PanelVendas(vendaController, usuarioLogado), "vendas");
        contentPanel.add(new PanelClientes(clienteController), "clientes");
        contentPanel.add(new PanelFornecedores(fornecedorController, produtoController), "fornecedores");
        contentPanel.add(new PanelRelatorios(relatorioController), "relatorios"); // Este limparemos na fase final
        contentPanel.add(new PanelCategorias(categoriaController, produtoController), "categorias");
        contentPanel.add(new PanelPerfil(usuarioLogado, usuarioController, lblUsuario), "perfil");

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        botaoSelecionado = btnDash;
        atualizarEstiloMenu();

        revalidate();
        repaint();
        setVisible(true);
    }

    private void alternarMenu() {
        menuAberto = !menuAberto;
        if (menuAberto) {
            sidebar.setPreferredSize(new Dimension(240, 0));
            lblUsuario.setVisible(true);
            for (JButton btn : botoesMenu) {
                btn.setVisible(true);
                btn.setText(btn.getActionCommand());
                btn.setHorizontalAlignment(SwingConstants.LEFT);
            }
        } else {
            sidebar.setPreferredSize(new Dimension(65, 0));
            lblUsuario.setVisible(false);
            for (JButton btn : botoesMenu) {
                btn.setVisible(false); // Esconde texto e botões no modo fechado
            }
        }
        atualizarEstiloMenu();
        sidebar.revalidate();
        sidebar.repaint();
    }
    
    private void atualizarEstiloMenu() {
        for (JButton btn : botoesMenu) {
            if(btn.getActionCommand().equals("Sair do Sistema")) continue;

            if (btn == botaoSelecionado) {
                btn.setBackground(SIDEBAR_BTN_SELECTED);
                btn.setForeground(Color.WHITE);
                btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
                
                if(menuAberto) {
                    btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 5, 0, 0, new Color(255, 255, 255, 100)),
                        BorderFactory.createEmptyBorder(10, 20, 10, 10)
                    ));
                }
            } else {
                btn.setBackground(SIDEBAR_BG);
                btn.setForeground(TEXT_COLOR);
                btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 10));
            }
        }
    }

    private JButton adicionarBotaoMenu(JPanel sidebar, String texto, String cardName) {
        JButton btn = new JButton(texto);
        btn.setActionCommand(texto);
        btn.setMaximumSize(new Dimension(240, 45));
        // CORREÇÃO 3: java.awt.Component explícito
        btn.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.setBackground(SIDEBAR_BG);
        btn.setForeground(TEXT_COLOR);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 10));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if(btn != botaoSelecionado) btn.setBackground(SIDEBAR_BTN_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if(btn != botaoSelecionado) btn.setBackground(SIDEBAR_BG);
            }
        });

        btn.addActionListener(e -> {
            if(cardName.equals("sair")) {
                System.exit(0);
            } else {
                botaoSelecionado = btn;
                atualizarEstiloMenu();
                cardLayout.show(contentPanel, cardName);
                
                // Refresh das telas
                // CORREÇÃO 4: Loop usando java.awt.Component
                for(java.awt.Component c : contentPanel.getComponents()) {
                    if (c.isVisible()) {
                        if(c instanceof PanelProdutos) ((PanelProdutos)c).carregarDados();
                        if(c instanceof PanelDashboard) ((PanelDashboard)c).atualizarDados();
                        if(c instanceof PanelVendas) ((PanelVendas)c).carregarVendas();
                        if(c instanceof PanelClientes) ((PanelClientes)c).carregarDados();
                        if(c instanceof PanelFornecedores) ((PanelFornecedores)c).carregarDados();
                        if(c instanceof PanelRelatorios) ((PanelRelatorios)c).calcularRelatorio();
                        if(c instanceof PanelCategorias) ((PanelCategorias)c).carregarDados();
                    }
                }
            }
        });

        botoesMenu.add(btn);
        sidebar.add(btn);
        return btn;
    }

    // --- CLASSE PARA DESENHAR O ÍCONE (MANUALMENTE) ---
    private static class IconeMenu implements Icon {
        private int tamanho;
        private Color cor;

        public IconeMenu(int tamanho, Color cor) {
            this.tamanho = tamanho;
            this.cor = cor;
        }

        // CORREÇÃO 5: java.awt.Component no argumento
        @Override
        public void paintIcon(java.awt.Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(cor);
            
            int h = 3; // Altura da linha
            int gap = 5; // Espaço entre linhas
            
            // Desenha 3 retângulos (as 3 listras do menu)
            g2.fillRoundRect(x, y + 2, tamanho, h, 2, 2);
            g2.fillRoundRect(x, y + 2 + h + gap, tamanho, h, 2, 2);
            g2.fillRoundRect(x, y + 2 + (h + gap)*2, tamanho, h, 2, 2);
            
            g2.dispose();
        }

        @Override
        public int getIconWidth() { return tamanho; }

        @Override
        public int getIconHeight() { return tamanho; }
    }
}