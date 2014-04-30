/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.Apresentacao;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.AtividadeTipo;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.AtividadeRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.Repositorio;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.GenericDataModel;
import java.util.List;

/**
 *
 * @author petronio
 */
public class AtividadeTipoDataModel extends GenericDataModel<AtividadeTipo> {

    AtividadeRepositorio daoA;
    
    public AtividadeTipoDataModel(List<AtividadeTipo> data, Repositorio<AtividadeTipo> repo) {
        super(data, repo);
    }

    public void setAtividadeRepositorio(AtividadeRepositorio daoA) {
        this.daoA = daoA;
    }
       
    @Override
    public AtividadeTipo getRowData(String rowKey) {
        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data  

        if (rowKey == null || rowKey.equals("")) {
            return null;
        }

        try {
            Long id = Long.parseLong(rowKey);

            AtividadeTipo obj = daoA.AbrirTipo(id);

            return obj;
        } catch (Exception ex) {
            return null;
        }
    }
    
}
