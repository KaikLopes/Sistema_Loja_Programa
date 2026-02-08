package br.com.sualoja.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ReparadorBanco implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>> VERIFICANDO INTEGRIDADE DO BANCO DE DADOS...");

        try {
            // 1. Tenta corrigir produtos com ativo NULO
            jdbcTemplate.execute("UPDATE produtos SET ativo = TRUE WHERE ativo IS NULL");
            
            // 2. Tenta corrigir quantidade_inicial NULA
            // Se for nulo, assume que a quantidade inicial é igual ao estoque atual
            jdbcTemplate.execute("UPDATE produtos SET quantidade_inicial = quantidade_estoque WHERE quantidade_inicial IS NULL");
            
            System.out.println(">>> BANCO DE DADOS CORRIGIDO COM SUCESSO!");
        } catch (Exception e) {
            // Se der erro, provavelmente as colunas ainda não existem, o que é normal na primeira vez
            System.out.println(">>> Aviso: Ajuste fino do banco pulado (normal se for a primeira execução).");
        }
    }
}