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

    @Autowired private ProdutoDAO produtoDAO;
    @Autowired private ClienteDAO clienteDAO;
    @Autowired private VendaItemDAO vendaItemDAO;
    @Autowired private VendaDAO vendaDAO;
    @Autowired private FornecedorDAO fornecedorDAO;
    @Autowired private CategoriaDAO categoriaDAO;

    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JPanel sidebar;
    private boolean menuAberto = true;
    private List<JButton> botoesMenu = new ArrayList<>();
    private JLabel lblUsuario;
    private JButton botaoSelecionado;

    // Cores da Sidebar
    private final Color SIDEBAR_BG = new Color(30, 30, 40);
    private final Color SIDEBAR_BTN_HOVER = new Color(50, 50, 65);
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
        // --- SIDEBAR ---
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(240, 0));

        // Cabeçalho Menu (Onde fica o botão de abrir/fechar)
        JPanel menuHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 25));
        menuHeader.setBackground(SIDEBAR_BG);
        menuHeader.setMaximumSize(new Dimension(240, 80));
        menuHeader.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

        JButton btnToggle = new JButton("☰");
        btnToggle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btnToggle.setForeground(Color.WHITE);
        btnToggle.setContentAreaFilled(false);
        btnToggle.setBorderPainted(false);
        btnToggle.setFocusPainted(false);
        btnToggle.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnToggle.addActionListener(e -> alternarMenu());

        menuHeader.add(btnToggle);
        sidebar.add(menuHeader);

        // Label Usuário
        String primeiroNome = usuarioLogado.getNome().split(" ")[0];
        lblUsuario = new JLabel("Olá, " + primeiroNome);
        lblUsuario.setForeground(Color.WHITE);
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblUsuario.setBorder(BorderFactory.createEmptyBorder(0, 25, 30, 0));
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
        btnSair.setForeground(new Color(255, 100, 100));

        // --- CONTEÚDO PRINCIPAL ---
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        contentPanel.add(new PanelDashboard(produtoDAO, vendaItemDAO, vendaDAO), "dashboard");
        contentPanel.add(new PanelProdutos(produtoController, fornecedorDAO, categoriaDAO, produtoDAO), "produtos");
        contentPanel.add(new PanelVendas(vendaController, clienteDAO, produtoDAO, vendaItemDAO, usuarioLogado), "vendas");
        contentPanel.add(new PanelClientes(clienteDAO), "clientes");
        contentPanel.add(new PanelFornecedores(fornecedorDAO, produtoDAO), "fornecedores");
        contentPanel.add(new PanelRelatorios(vendaDAO, produtoDAO), "relatorios");
        contentPanel.add(new PanelCategorias(categoriaController, produtoDAO), "categorias");
        contentPanel.add(new PanelPerfil(usuarioLogado, usuarioController, lblUsuario), "perfil");

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        botaoSelecionado = btnDash;
        atualizarEstiloMenu(); // Garante o estilo inicial

        setVisible(true);
    }

    private void alternarMenu() {
        menuAberto = !menuAberto;

        if (menuAberto) {
            // ABRINDO: Mostra tudo
            sidebar.setPreferredSize(new Dimension(240, 0));
            lblUsuario.setVisible(true);

            for (JButton btn : botoesMenu) {
                btn.setVisible(true); // REAPARECE OS BOTÕES
                btn.setText(btn.getActionCommand());
                btn.setHorizontalAlignment(SwingConstants.LEFT);
            }
        } else {
            // FECHANDO: Esconde botões e deixa barra fina
            sidebar.setPreferredSize(new Dimension(65, 0));
            lblUsuario.setVisible(false);

            for (JButton btn : botoesMenu) {
                btn.setVisible(false); // <--- AQUI ESTÁ O TRUQUE: ESCONDE TOTALMENTE
            }
        }

        atualizarEstiloMenu();
        sidebar.revalidate();
        sidebar.repaint();
    }

    private void atualizarEstiloMenu() {
        for (JButton btn : botoesMenu) {
            if (btn.getActionCommand().equals("Sair do Sistema")) continue;

            if (btn == botaoSelecionado) {
                btn.setBackground(SIDEBAR_BTN_HOVER);
                btn.setForeground(Color.WHITE);
                btn.setFont(new Font("Segoe UI", Font.BOLD, 14));

                // Borda lateral azul apenas se o menu estiver aberto
                if (menuAberto) {
                    btn.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createMatteBorder(0, 5, 0, 0, SIDEBAR_BTN_SELECTED),
                            BorderFactory.createEmptyBorder(10, 20, 10, 10)
                    ));
                } else {
                    btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                }
            } else {
                btn.setBackground(SIDEBAR_BG);
                btn.setForeground(TEXT_COLOR);
                btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));

                if (menuAberto) {
                    btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 10));
                } else {
                    btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                }
            }
        }
    }

    private JButton adicionarBotaoMenu(JPanel sidebar, String texto, String cardName) {
        JButton btn = new JButton(texto);
        btn.setActionCommand(texto);
        btn.setMaximumSize(new Dimension(240, 45));
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
            if (cardName.equals("sair")) {
                System.exit(0);
            } else {
                botaoSelecionado = btn;
                atualizarEstiloMenu();
                cardLayout.show(contentPanel, cardName);

                // Refresh nos dados quando troca de aba
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
}