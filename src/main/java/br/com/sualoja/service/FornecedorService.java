package br.com.sualoja.service;

import br.com.sualoja.dao.FornecedorDAO;
import br.com.sualoja.model.Fornecedor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FornecedorService {

    @Autowired
    private FornecedorDAO fornecedorDAO;

    public List<Fornecedor> buscarTodos() {
        return fornecedorDAO.findAllByOrderByNomeFantasiaAsc();
    }

    public Fornecedor buscarPorId(Long id) {
        return fornecedorDAO.findById(id).orElse(null);
    }

    @Transactional
    public void salvar(Fornecedor fornecedor) {
        validarRegras(fornecedor);
        fornecedorDAO.save(fornecedor);
    }

    @Transactional
    public void atualizar(Long id, Fornecedor fornecedorAtualizado) {
        Fornecedor fornecedorExistente = fornecedorDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado!"));

        validarRegras(fornecedorAtualizado);

        fornecedorExistente.setNomeFantasia(fornecedorAtualizado.getNomeFantasia());
        fornecedorExistente.setRazaoSocial(fornecedorAtualizado.getRazaoSocial());
        fornecedorExistente.setCnpj(fornecedorAtualizado.getCnpj());

        fornecedorDAO.save(fornecedorExistente);
    }

    @Transactional
    public void deletar(Long id) {
        if (!fornecedorDAO.existsById(id)) {
            throw new RuntimeException("Fornecedor não encontrado para exclusão.");
        }
        fornecedorDAO.deleteById(id);
    }

    private void validarRegras(Fornecedor f) {
        if (f.getNomeFantasia() == null || f.getNomeFantasia().trim().isEmpty()) {
            throw new RuntimeException("O Nome Fantasia é obrigatório!");
        }

        String cnpjLimpo = f.getCnpj() != null ? f.getCnpj().replaceAll("[^0-9]", "") : "";
        if (cnpjLimpo.length() != 14 && cnpjLimpo.length() > 0) {
            // Permite vazio (se não for obrigatório), mas se preencher, tem que ter 14
            throw new RuntimeException("CNPJ Inválido! Deve ter exatamente 14 números.");
        }
        f.setCnpj(cnpjLimpo); // Limpa a formatação antes de ir pro banco
    }
}