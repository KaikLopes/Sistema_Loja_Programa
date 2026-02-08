package br.com.sualoja.view;

import br.com.sualoja.controller.CategoriaController;
import br.com.sualoja.controller.ProdutoController;
import br.com.sualoja.controller.VendaController;
import br.com.sualoja.dao.CategoriaDAO;
import br.com.sualoja.dao.ClienteDAO;
import br.com.sualoja.dao.FornecedorDAO;
import br.com.sualoja.dao.ProdutoDAO;
import br.com.sualoja.dao.VendaDAO;
import br.com.sualoja.dao.VendaItemDAO;
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

    public TelaPrincipal() {
        setTitle("Sua Loja - Gestão Profissional");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    public void iniciar(Usuario usuarioLogado) {
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(33, 41, 54)); 
        sidebar.setPreferredSize(new Dimension(240, 0)); 

        JPanel menuHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 25));
        menuHeader.setBackground(new Color(33, 41, 54));
        menuHeader.setMaximumSize(new Dimension(240, 80));
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
        
        String primeiroNome = usuarioLogado.getNome().split(" ")[0]; 
        lblUsuario = new JLabel("Olá, " + primeiroNome);
        lblUsuario.setForeground(Color.WHITE);
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblUsuario.setBorder(BorderFactory.createEmptyBorder(0, 25, 30, 0));
        lblUsuario.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        sidebar.add(lblUsuario);

        JButton btnDash = adicionarBotaoMenu(sidebar, "Dashboard", "dashboard");
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        
        adicionarBotaoMenu(sidebar, "Produtos", "produtos");
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        
        adicionarBotaoMenu(sidebar, "Categorias", "categorias");
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        
        adicionarBotaoMenu(sidebar, "Vendas", "vendas");
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        
        adicionarBotaoMenu(sidebar, "Clientes", "clientes");
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        
        adicionarBotaoMenu(sidebar, "Fornecedores", "fornecedores");
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        
        adicionarBotaoMenu(sidebar, "Relatórios", "relatorios");

        sidebar.add(Box.createVerticalGlue());
        JButton btnSair = adicionarBotaoMenu(sidebar, "Sair do Sistema", "sair");
        btnSair.setBackground(new Color(220, 53, 69));
        btnSair.setForeground(Color.WHITE);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        
        contentPanel.add(new PanelDashboard(produtoDAO, vendaItemDAO, vendaDAO), "dashboard");
        contentPanel.add(new PanelProdutos(produtoController, fornecedorDAO, categoriaDAO, produtoDAO), "produtos");
        contentPanel.add(new PanelVendas(vendaController, clienteDAO, produtoDAO, vendaItemDAO, usuarioLogado), "vendas");
        contentPanel.add(new PanelClientes(clienteDAO), "clientes");
        contentPanel.add(new PanelFornecedores(fornecedorDAO, produtoDAO), "fornecedores");
        contentPanel.add(new PanelRelatorios(vendaDAO, produtoDAO), "relatorios");
        
        // --- AQUI: Passa controller E produtoDAO ---
        contentPanel.add(new PanelCategorias(categoriaController, produtoDAO), "categorias");

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        botaoSelecionado = btnDash;
        atualizarEstiloMenu();

        setVisible(true);
    }

    private void alternarMenu() {
        menuAberto = !menuAberto;
        if (menuAberto) {
            sidebar.setPreferredSize(new Dimension(240, 0));
            lblUsuario.setVisible(true);
            for (JButton btn : botoesMenu) {
                btn.setText(btn.getActionCommand());
                btn.setHorizontalAlignment(SwingConstants.LEFT);
            }
        } else {
            sidebar.setPreferredSize(new Dimension(70, 0));
            lblUsuario.setVisible(false);
            for (JButton btn : botoesMenu) {
                btn.setText("");
            }
        }
        sidebar.revalidate();
        sidebar.repaint();
    }
    
    private void atualizarEstiloMenu() {
        Color corFundoPadrao = new Color(33, 41, 54);
        Color corTextoPadrao = new Color(200, 200, 200);
        Color corFundoSelecionado = new Color(45, 55, 72); 
        Color corTextoSelecionado = Color.WHITE;
        Color corBordaSelecionado = new Color(0, 123, 255); 

        for (JButton btn : botoesMenu) {
            if(btn.getActionCommand().equals("Sair do Sistema")) continue;

            if (btn == botaoSelecionado) {
                btn.setBackground(corFundoSelecionado);
                btn.setForeground(corTextoSelecionado);
                btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
                btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 4, 0, 0, corBordaSelecionado),
                    BorderFactory.createEmptyBorder(10, 21, 10, 0)
                ));
            } else {
                btn.setBackground(corFundoPadrao);
                btn.setForeground(corTextoPadrao);
                btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
                btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 0));
            }
        }
    }

    private JButton adicionarBotaoMenu(JPanel sidebar, String texto, String cardName) {
        JButton btn = new JButton(texto);
        btn.setActionCommand(texto);
        btn.setMaximumSize(new Dimension(240, 50));
        btn.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        btn.setBackground(new Color(33, 41, 54));
        btn.setForeground(new Color(200, 200, 200)); 
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 0));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if(btn != botaoSelecionado && !texto.equals("Sair do Sistema")) {
                    btn.setBackground(new Color(40, 48, 60)); 
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if(btn != botaoSelecionado && !texto.equals("Sair do Sistema")) {
                    btn.setBackground(new Color(33, 41, 54)); 
                }
            }
        });

        btn.addActionListener(e -> {
            if(cardName.equals("sair")) {
                System.exit(0);
            } else {
                botaoSelecionado = btn;
                atualizarEstiloMenu();
                cardLayout.show(contentPanel, cardName);
                
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

    private static class IconeMenu implements Icon {
        private int tamanho;
        private Color cor;
        public IconeMenu(int tamanho, Color cor) { this.tamanho = tamanho; this.cor = cor; }
        @Override
        public void paintIcon(java.awt.Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(cor);
            int alturaLinha = 3; int largura = tamanho; int gap = 6; 
            int startY = y + (getIconHeight() - (alturaLinha*3 + gap*2)) / 2;
            g2.fillRoundRect(x, startY, largura, alturaLinha, 2, 2);
            g2.fillRoundRect(x, startY + gap + alturaLinha, largura, alturaLinha, 2, 2);
            g2.fillRoundRect(x, startY + (gap + alturaLinha) * 2, largura, alturaLinha, 2, 2);
            g2.dispose();
        }
        @Override public int getIconWidth() { return tamanho; }
        @Override public int getIconHeight() { return tamanho; }
    }
}