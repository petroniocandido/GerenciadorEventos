/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Presentation;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Recurso;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.RecursoRepositorio;
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
@Named(value = "recursoConverter")
@SessionScoped
public class RecursoConverter
        extends GenericConverter<Recurso, RecursoRepositorio>
        implements Serializable {

    @EJB
    RecursoRepositorio dao;

    @PostConstruct
    public void init() {
        setRepositorio(dao);
    }
}
