package br.com.sualoja.model;

import jakarta.persistence.*;

@Entity
@Table(name = "fornecedores")
public class Fornecedor {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String razaoSocial;
    private String nomeFantasia;
    private String cnpj;

    public Fornecedor() {}

    public Fornecedor(String nomeFantasia, String cnpj) {
        this.nomeFantasia = nomeFantasia;
        this.cnpj = cnpj;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRazaoSocial() { return razaoSocial; }
    public void setRazaoSocial(String razaoSocial) { this.razaoSocial = razaoSocial; }
    public String getNomeFantasia() { return nomeFantasia; }
    public void setNomeFantasia(String nomeFantasia) { this.nomeFantasia = nomeFantasia; }
    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    @Override
    public String toString() { return this.nomeFantasia; }
}