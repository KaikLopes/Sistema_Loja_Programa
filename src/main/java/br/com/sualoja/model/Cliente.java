package br.com.sualoja.model;

import jakarta.persistence.*;

@Entity
@Table(name = "clientes")
@PrimaryKeyJoinColumn(name = "pessoa_id") // <--- ISSO CONSERTA O ERRO DE SQL
public class Cliente extends Pessoa {

    private String endereco;
    private String telefone;

    public Cliente() {
        super();
    }

    public Cliente(String nome, String cpf, String endereco, String telefone) {
        super(nome, cpf); // Passa nome e cpf para a tabela pai (Pessoas)
        this.endereco = endereco;
        this.telefone = telefone;
    }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    
    // IMPORTANTE: Isso faz o combobox mostrar o nome, e não o código estranho
    @Override
    public String toString() {
        return this.getNome(); 
    }
}