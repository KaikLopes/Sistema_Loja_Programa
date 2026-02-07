package br.com.sualoja;

import br.com.sualoja.view.TelaPrincipal; 
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import java.awt.EventQueue;

@SpringBootApplication
public class LojaApplication {

    public static void main(String[] args) {
        // 1. Configuração para dizer "Não sou um site, sou um app Desktop"
        ConfigurableApplicationContext context = new SpringApplicationBuilder(LojaApplication.class)
                .headless(false) 
                .web(WebApplicationType.NONE) 
                .run(args);

        // 2. Inicia a tela visual
        EventQueue.invokeLater(() -> {
            // Se der erro aqui, é porque ainda não criamos a classe TelaPrincipal (faremos no passo 2)
            TelaPrincipal tela = context.getBean(TelaPrincipal.class);
            tela.setVisible(true);
        });
    }
}