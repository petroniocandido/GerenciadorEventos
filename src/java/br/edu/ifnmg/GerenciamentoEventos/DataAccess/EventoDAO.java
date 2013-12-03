/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.DataAccess;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.*;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author petronio
 */
@Stateless
public class EventoDAO 
    extends DAOGenerico<Evento> 
    implements EventoRepositorio {
    
    public EventoDAO(){
        super(Evento.class);
    }

    @Override
    public List<Evento> Buscar(Evento filtro) {
        return IgualA("nome", filtro.getNome())
                .IgualA("inicio", filtro.getInicio())
                .IgualA("termino", filtro.getTermino())
                .Buscar();
    }
    
}
