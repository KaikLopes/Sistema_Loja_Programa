package br.com.sualoja.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Usuarios")
@PrimaryKeyJoinColumn(name = "pessoa_id") // Liga com o ID da tabela Pessoas
public class Usuario extends Pessoa {

    private String login;
    
    @Column(name = "senha_hash")
    private String senha;

    public Usuario() {}

    public Usuario(String nome, String cpf, String login, String senha) {
        super(nome, cpf); 
        this.login = login;
        this.senha = senha;
    }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}