package br.com.sualoja.service;

import br.com.sualoja.dao.ProdutoDAO;
import br.com.sualoja.model.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal; // Importante para o seu preço
import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoDAO produtoDAO;

    public List<Produto> listarTodos() {
        return produtoDAO.findAll();
    }

    public void salvar(Produto produto) {
        // Validação: Preço maior que zero
        if (produto.getPrecoVenda().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O preço não pode ser negativo.");
        }
        produtoDAO.save(produto);
    }

    public void atualizar(Integer id, Produto produtoAtualizado) {
        // Busca o antigo (Repare que o ID é Integer)
        Produto antigo = produtoDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        // Atualiza os dados
        antigo.setNome(produtoAtualizado.getNome());
        antigo.setPrecoVenda(produtoAtualizado.getPrecoVenda()); // BigDecimal
        antigo.setQuantidadeEstoque(produtoAtualizado.getQuantidadeEstoque());

        produtoDAO.save(antigo);
    }

    public void deletar(Integer id) {
        produtoDAO.deleteById(id);
    }
}