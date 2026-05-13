package br.com.sualoja.service;

import br.com.sualoja.dao.ProdutoDAO;
import br.com.sualoja.model.Produto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoDAO produtoDAO;

    public ProdutoService(ProdutoDAO produtoDAO) {
        this.produtoDAO = produtoDAO;
    }

    public List<Produto> listarTodos() {
        return produtoDAO.findAll();
    }

    // --- MÉTODO NOVO PARA A TELA DE CATEGORIAS ---
    public List<Produto> buscarPorCategoria(Integer idCategoria) {
        return produtoDAO.findByCategoriaId(idCategoria);
    }

    @Transactional
    public void salvar(Produto produto) {
        validarRegrasDeNegocio(produto);
        produtoDAO.save(produto);
    }

    @Transactional
    public void atualizar(Integer id, Produto produtoAtualizado) {
        Produto antigo = produtoDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado no banco."));

        validarRegrasDeNegocio(produtoAtualizado);

        // Mapeamento completo: garante que NENHUM dado se perca na edição
        antigo.setNome(produtoAtualizado.getNome());
        antigo.setPrecoVenda(produtoAtualizado.getPrecoVenda());
        antigo.setUltimoPrecoCompra(produtoAtualizado.getUltimoPrecoCompra());
        antigo.setQuantidadeEstoque(produtoAtualizado.getQuantidadeEstoque());
        antigo.setFornecedor(produtoAtualizado.getFornecedor());
        antigo.setCategoria(produtoAtualizado.getCategoria());
        antigo.setAtivo(produtoAtualizado.getAtivo());

        produtoDAO.save(antigo);
    }

    @Transactional
    public void deletar(Integer id) {
        if (!produtoDAO.existsById(id)) {
            throw new RuntimeException("Produto não encontrado para exclusão!");
        }
        produtoDAO.deleteById(id);
    }

    public List<Produto> buscarPorFornecedor(Long idFornecedor) {
        return produtoDAO.findByFornecedorId(idFornecedor);
    }

    // --- CENTRALIZAÇÃO DE REGRAS (DRY - Don't Repeat Yourself) ---
    private void validarRegrasDeNegocio(Produto p) {
        if (p.getNome() == null || p.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do produto é obrigatório!");
        }
        if (p.getPrecoVenda() == null || p.getPrecoVenda().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O preço de venda deve ser maior que zero.");
        }
        if (p.getQuantidadeEstoque() == null || p.getQuantidadeEstoque() < 0) {
            throw new IllegalArgumentException("A quantidade em estoque não pode ser negativa.");
        }
    }
}
