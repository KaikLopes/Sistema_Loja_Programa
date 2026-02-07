package br.com.sualoja;

import br.com.sualoja.view.TelaLogin; // <--- Mudou o import
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import java.awt.EventQueue;
import com.formdev.flatlaf.FlatIntelliJLaf;

@SpringBootApplication
public class LojaApplication {

    public static void main(String[] args) {
        FlatIntelliJLaf.setup(); 

        ConfigurableApplicationContext context = new SpringApplicationBuilder(LojaApplication.class)
                .headless(false)
                .web(WebApplicationType.NONE)
                .run(args);

        EventQueue.invokeLater(() -> {
            TelaLogin login = context.getBean(TelaLogin.class);
            login.iniciar();
        });
    }
}