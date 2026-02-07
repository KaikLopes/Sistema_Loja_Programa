package br.com.sualoja.view;

import org.springframework.stereotype.Component;
import javax.swing.*;

@Component 
public class TelaPrincipal extends JFrame {

    public TelaPrincipal() {
        setTitle("Sistema Loja - Sucesso!");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JLabel label = new JLabel("Backend Migrado com Sucesso!", SwingConstants.CENTER);
        add(label);
    }
}