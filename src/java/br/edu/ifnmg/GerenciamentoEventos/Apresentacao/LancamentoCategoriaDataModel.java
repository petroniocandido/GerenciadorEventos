/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.Apresentacao;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.LancamentoCategoria;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.LancamentoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.Repositorio;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.GenericDataModel;
import java.util.List;

/**
 *
 * @author petronio
 */
public class LancamentoCategoriaDataModel extends GenericDataModel<LancamentoCategoria> {

    LancamentoRepositorio daoA;
    
    public LancamentoCategoriaDataModel(List<LancamentoCategoria> data, Repositorio<LancamentoCategoria> repo) {
        super(data, repo);
    }

    public void setLancamentoRepositorio(LancamentoRepositorio daoA) {
        this.daoA = daoA;
    }
       
    @Override
    public LancamentoCategoria getRowData(String rowKey) {
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data  

        if (rowKey == null || rowKey.equals("")) {
            return null;
        }

        try {
            Long id = Long.parseLong(rowKey);

            LancamentoCategoria obj = daoA.AbrirCategoria(id);

            return obj;
        } catch (Exception ex) {
            return null;
        }
    }
    
}
