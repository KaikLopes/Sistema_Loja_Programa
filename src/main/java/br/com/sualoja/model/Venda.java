package br.com.sualoja.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Vendas")
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_venda")
    private LocalDateTime dataVenda = LocalDateTime.now();

    @Column(name = "valor_total")
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @ManyToOne // Muitas vendas para um cliente
    @JoinColumn(name = "cliente_pessoa_id")
    private Cliente cliente;

    @ManyToOne // Muitas vendas para um usuário
    @JoinColumn(name = "usuario_pessoa_id")
    private Usuario vendedor;

    // A Venda "manda" nos itens. Cascade ALL significa: salvou venda, salva itens.
    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL)
    private List<VendaItem> itens = new ArrayList<>();

    public Venda() {}

    public Venda(Cliente cliente, Usuario vendedor) {
        this.cliente = cliente;
        this.vendedor = vendedor;
    }

    public void adicionarItem(VendaItem item) {
        item.setVenda(this);
        this.itens.add(item);
        this.valorTotal = this.valorTotal.add(item.getPrecoUnitario().multiply(new BigDecimal(item.getQuantidade())));
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getDataVenda() { return dataVenda; }
    public void setDataVenda(LocalDateTime dataVenda) { this.dataVenda = dataVenda; }

    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Usuario getVendedor() { return vendedor; }
    public void setVendedor(Usuario vendedor) { this.vendedor = vendedor; }

    public List<VendaItem> getItens() { return itens; }
    public void setItens(List<VendaItem> itens) { this.itens = itens; }
}