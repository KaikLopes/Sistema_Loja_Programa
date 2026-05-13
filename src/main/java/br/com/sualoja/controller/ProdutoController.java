package br.com.sualoja.controller;

import br.com.sualoja.dao.CategoriaDAO;
import br.com.sualoja.dao.FornecedorDAO;
import br.com.sualoja.model.Categoria;
import br.com.sualoja.model.Fornecedor;
import br.com.sualoja.model.Produto;
import br.com.sualoja.service.ProdutoService;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
public class ProdutoController {

    private final ProdutoService service;

    // --- INJETAMOS OS DAOs AQUI PARA PROTEGER A TELA ---
    private final CategoriaDAO categoriaDAO;

    private final FornecedorDAO fornecedorDAO;

    public ProdutoController(ProdutoService service, CategoriaDAO categoriaDAO, FornecedorDAO fornecedorDAO) {
        this.service = service;
        this.categoriaDAO = categoriaDAO;
        this.fornecedorDAO = fornecedorDAO;
    }

    public List<Produto> buscarTodos() {
        return service.listarTodos();
    }

    // O Controller agora exige o objeto Produto completo, montado pela tela!
    public void salvar(Produto produto) {
        if (produto.getId() != null) {
            service.atualizar(produto.getId(), produto);
        } else {
            service.salvar(produto);
        }
    }

    public void excluir(Integer id) {
        service.deletar(id);
    }

    // O método de resumo que acabamos de criar
    public String obterResumoProdutosDaCategoria(Integer idCategoria) {
        List<Produto> produtos = service.buscarPorCategoria(idCategoria);
        StringBuilder sb = new StringBuilder("Produtos nesta Categoria:\n\n");

        boolean temProduto = false;
        for(Produto p : produtos) {
            if(p.getAtivo()) {
                sb.append("- ").append(p.getNome())
                        .append(" (Estoque: ").append(p.getQuantidadeEstoque()).append(")\n");
                temProduto = true;
            }
        }

        if(!temProduto) {
            sb.append("Nenhum produto cadastrado ou ativo.");
        }

        return sb.toString();
    }

    // ====================================================================
    // MÉTODOS NOVOS PARA ABASTECER O PANEL PRODUTOS
    // ====================================================================

    public List<Categoria> buscarCategorias() {
        return categoriaDAO.findAllByOrderByNomeAsc();
    }

    public List<Fornecedor> buscarFornecedores() {
        return fornecedorDAO.findAllByOrderByNomeFantasiaAsc();
    }

    public String obterResumoProdutosDoFornecedor(Long idFornecedor) {
        List<Produto> produtos = service.buscarPorFornecedor(idFornecedor);
        StringBuilder sb = new StringBuilder("Produtos deste fornecedor:\n\n");
        if(produtos.isEmpty()) {
            sb.append("Nenhum produto cadastrado para este fornecedor.");
        }
        for(Produto p : produtos) {
            if(p.getAtivo()) {
                sb.append("- ").append(p.getNome()).append(" (Estoque: ").append(p.getQuantidadeEstoque()).append(")\n");
            }
        }
        return sb.toString();
    }

    public void arquivar(Integer id) {
        // Busca o produto, muda o status para inativo e salva
        Produto p = service.listarTodos().stream()
                .filter(prod -> prod.getId().equals(id))
                .findFirst().orElse(null);

        if (p != null) {
            p.setAtivo(false);
            this.salvar(p); // Reaproveitamos o seu método salvar!
        }
    }
}
