/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.DataAccess;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.EventoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.*;
import java.util.List;
import javax.ejb.Singleton;
import javax.ejb.Stateless;

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
    
}
