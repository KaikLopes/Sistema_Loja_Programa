package br.com.sualoja.model;

import jakarta.persistence.*;

@Entity
@Table(name = "clientes")
@PrimaryKeyJoinColumn(name = "pessoa_id")
public class Cliente extends Pessoa {

    public Cliente() {
        super();
    }

    public Cliente(String nome, String cpf, String endereco, String telefone) {
        super(nome, cpf);
        this.setEndereco(endereco);
        this.setTelefone(telefone);
    }
    
    @Override
    public String toString() {
        return this.getNome(); 
    }
}