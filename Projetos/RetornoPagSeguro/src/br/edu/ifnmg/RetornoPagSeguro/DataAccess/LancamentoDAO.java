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

package br.edu.ifnmg.RetornoPagSeguro.DataAccess;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Lancamento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.LancamentoCategoria;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.LancamentoRepositorio;
import java.util.List;

/**
 *
 * @author petronio
 */
public class LancamentoDAO 
    extends DAO<Lancamento> 
    implements LancamentoRepositorio {

    public LancamentoDAO(){
        super(Lancamento.class);
    }
    
    @Override
    public List<Lancamento> Buscar(Lancamento filtro) {
       return Buscar();
               
    }

    @Override
    public LancamentoCategoria AbrirCategoria(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LancamentoCategoria CategoriaPadrao() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean SalvarCategoria(LancamentoCategoria obj) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean ApagarCategoria(LancamentoCategoria obj) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<LancamentoCategoria> BuscarCategorias(LancamentoCategoria filtro) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<LancamentoCategoria> listagemCategorias() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
