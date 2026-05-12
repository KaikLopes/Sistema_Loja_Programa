package br.com.sualoja.service;

import br.com.sualoja.dao.CategoriaDAO;
import br.com.sualoja.model.Categoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaDAO categoriaDAO;

    public List<Categoria> listarTodos() {
        return categoriaDAO.findAllByOrderByNomeAsc();
    }

    public Categoria buscarPorId(Integer id) {
        return categoriaDAO.findById(id).orElse(null);
    }

    public List<Categoria> buscarPorNome(String nome) {
        return categoriaDAO.findAllByOrderByNomeAsc().stream()
                .filter(c -> c.getNome().toLowerCase().contains(nome.toLowerCase()))
                .toList();
    }

    @Transactional
    public void salvar(Categoria categoria) {
        if (categoria.getNome() == null || categoria.getNome().trim().isEmpty()) {
            throw new RuntimeException("Nome da categoria é obrigatório!");
        }
        categoriaDAO.save(categoria);
    }

    @Transactional
    public void atualizar(Integer id, Categoria categoriaAtualizada) {
        Categoria categoria = categoriaDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada!"));

        categoria.setNome(categoriaAtualizada.getNome());
        categoriaDAO.save(categoria);
    }

    @Transactional
    public void deletar(Integer id) {
        if (!categoriaDAO.existsById(id)) {
            throw new RuntimeException("Categoria não encontrada!");
        }
        categoriaDAO.deleteById(id);
    }
}