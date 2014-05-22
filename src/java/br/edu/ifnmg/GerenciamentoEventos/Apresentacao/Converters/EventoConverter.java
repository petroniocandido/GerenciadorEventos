/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao.Converters;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.EventoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.GenericConverter;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 *
 * @author petronio
 */
@Named(value = "eventoConverter")
@Singleton
public class EventoConverter
        extends GenericConverter<Evento, EventoRepositorio>
        implements Serializable {

    @EJB
    EventoRepositorio dao;

    @PostConstruct
    public void init() {
        setRepositorio(dao);
    }
    
     public List<Evento> autoCompleteEvento(String query) {
        Evento i = new Evento();
        i.setNome(query);
        return dao.Buscar(i);
    }
}
