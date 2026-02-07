package br.com.sualoja.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Pessoas")
@Inheritance(strategy = InheritanceType.JOINED) 
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String cpf;
    
    @Column(name = "criado_em")
    private LocalDateTime criadoEm = LocalDateTime.now();

    // Construtor vazio (obrigatório pro Hibernate)
    public Pessoa() {}

    public Pessoa(String nome, String cpf) {
        this.nome = nome;
        this.cpf = cpf;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
}