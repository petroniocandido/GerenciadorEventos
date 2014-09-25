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

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.ControleRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.*;
import java.util.List;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.persistence.LockModeType;

/**
 *
 * @author petronio
 */
@Singleton
public class ControleDAO 
    extends DAOGenerico<Controle> 
    implements ControleRepositorio {
    
    
    public ControleDAO(){
        super(Controle.class);
    }
    

    @Override
    public List<Controle> Buscar(Controle filtro) {
        IgualA("id", filtro.getId());
                
        return   Buscar();
    }

    @Override
    public Controle Abrir(Evento e) {
        return IgualA("evento", e).Abrir();
    }

    @Override
    public Controle Abrir(Atividade a) {
        return IgualA("atividade", a).Abrir();
    }
    
    public boolean Alterar(Controle obj){
        getManager().lock(obj, LockModeType.WRITE);
        return Salvar(obj);
    }

}
