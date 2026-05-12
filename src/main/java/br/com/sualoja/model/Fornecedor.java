package br.com.sualoja.model;

import jakarta.persistence.*;

@Entity
@Table(name = "fornecedores")
@PrimaryKeyJoinColumn(name = "id")
public class Fornecedor extends Pessoa {
    
    private String razaoSocial;
    private String nomeFantasia;
    private String cnpj;

    public Fornecedor() {
        super();
    }

    public Fornecedor(String nomeFantasia, String cnpj) {
        this.nomeFantasia = nomeFantasia;
        this.cnpj = cnpj;
    }

    public String getRazaoSocial() { return razaoSocial; }
    public void setRazaoSocial(String razaoSocial) { this.razaoSocial = razaoSocial; }
    public String getNomeFantasia() { return nomeFantasia; }
    public void setNomeFantasia(String nomeFantasia) { this.nomeFantasia = nomeFantasia; }
    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    @Override
    public String toString() { return this.nomeFantasia; }
}