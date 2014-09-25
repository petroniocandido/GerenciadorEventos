/*
 *   This file is part of SGEA - Sistema de Gestão de Eventos Acadêmicos - TADS IFNMG Campus Januária.
 *
 *   SGEA is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   SGEA is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with SGEA.  If not, see <http://www.gnu.org/licenses/>.
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
