/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.Infraestrutura.Dados;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.EventoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.Singleton;

/**
 *
 * @author petronio
 */
@Singleton
public class EventoDAO 
    extends DAOGenerico<Evento> 
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
}
