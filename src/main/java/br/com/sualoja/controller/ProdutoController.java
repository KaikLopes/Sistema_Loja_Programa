package br.com.sualoja.controller;

import br.com.sualoja.model.Produto;
import br.com.sualoja.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import java.math.BigDecimal;
import java.util.List;

@Controller
public class ProdutoController {

    @Autowired
    private ProdutoService service;

    public List<Produto> buscarTodos() {
        return service.listarTodos();
    }

    public void salvarProduto(String nome, String precoTexto, String qtdTexto) throws Exception {
        Produto p = criarProdutoDoTexto(nome, precoTexto, qtdTexto);
        service.salvar(p);
    }

    public void atualizarProduto(Integer id, String nome, String precoTexto, String qtdTexto) throws Exception {
        Produto p = criarProdutoDoTexto(nome, precoTexto, qtdTexto);
        service.atualizar(id, p);
    }

    public void excluirProduto(Integer id) {
        service.deletar(id);
    }

    // Método auxiliar para não repetir código
    private Produto criarProdutoDoTexto(String nome, String precoTexto, String qtdTexto) throws Exception {
        try {
            // Troca vírgula por ponto e cria o BigDecimal
            BigDecimal preco = new BigDecimal(precoTexto.replace(",", "."));
            Integer qtd = Integer.parseInt(qtdTexto);

            Produto p = new Produto();
            p.setNome(nome);
            p.setPrecoVenda(preco); // Setter correto da sua classe
            p.setQuantidadeEstoque(qtd); // Setter correto da sua classe

            return p;
        } catch (NumberFormatException e) {
            throw new Exception("Número inválido! Use formato: 10.50 ou 10");
        }
    }
}