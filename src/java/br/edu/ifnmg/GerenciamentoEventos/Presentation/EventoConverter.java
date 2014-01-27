/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Presentation;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.EventoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Presentation.Comum.GenericConverter;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author petronio
 */
@Named(value = "eventoConverter")
@SessionScoped
public class EventoConverter
        extends GenericConverter<Evento, EventoRepositorio>
        implements Serializable {

    @EJB
    EventoRepositorio dao;

    @PostConstruct
    public void init() {
        setRepositorio(dao);
    }
}
