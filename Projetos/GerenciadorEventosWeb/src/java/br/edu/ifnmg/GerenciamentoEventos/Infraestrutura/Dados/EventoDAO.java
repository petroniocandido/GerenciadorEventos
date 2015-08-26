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

import br.edu.ifnmg.DomainModel.Pessoa;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.EventoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.Singleton;
import javax.persistence.Query;

/**
 *
 * @author petronio
 */
@Singleton
public class EventoDAO 
    extends DAO<Evento> 
    implements EventoRepositorio {
    
    public EventoDAO(){
        super(Evento.class);
    }

    @Override
    public List<Evento> Buscar(Evento filtro) {
        return IgualA("id", filtro.getId())
                .Like("nome", filtro.getNome())
                .Buscar();
    }
    
    @Override
    public boolean Salvar(Evento e){
        if(e.getControle() == null){
            e.setControle(new Controle(e, 0, 0));
        }
        return super.Salvar(e);
    }

    @Override
    public List<Evento> BuscarEventosDoUsuario(Pessoa obj) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, 1);
        
        return MaiorOuIgualA("termino", cal.getTime())
                .DiferenteDe("status", Status.Cancelado)
                .DiferenteDe("status", Status.Concluido)
                .Buscar();
    }
    
    @Override
    public List<Evento> Responsavel(Pessoa obj){
        Query q =getManager()
                .createNamedQuery("eventos.responsavel")
                .setParameter("idUsuario", obj.getId())
                .setHint("eclipselink.QUERY_RESULTS_CACHE", "TRUE");
        return q.getResultList();
    }
}
