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

package br.edu.ifnmg.GerenciamentoEventos.Infraestrutura.Dados;

import br.edu.ifnmg.DataAccess.DAOGenerico;
import br.edu.ifnmg.DomainModel.Log;
import br.edu.ifnmg.DomainModel.Services.LogRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.*;
import java.util.List;
import javax.ejb.Singleton;
import javax.ejb.Stateless;

/**
 *
 * @author petronio
 */
@Singleton
public class LogDAO 
    extends DAO<Log> 
    implements LogRepositorio {

    public LogDAO(){
        super(Log.class);
    }
    
    @Override
    public List<Log> Buscar(Log filtro) {
       return MaiorOuIgualA("dataEvento", filtro.getDataEvento())
                .IgualA("usuario", filtro.getUsuario())
                .IgualA("permissao", filtro.getPermissao())    
                .Ordenar("dataEvento", "desc")
                .Buscar();
               
    }
    
}
