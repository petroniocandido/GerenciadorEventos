/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Presentation;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Inscricao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.InscricaoRepositorio;
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
@Named(value = "inscricaoConverter")
@SessionScoped
public class InscricaoConverter
        extends GenericConverter<Inscricao, InscricaoRepositorio>
        implements Serializable {

    @EJB
    InscricaoRepositorio dao;

    @PostConstruct
    public void init() {
        setRepositorio(dao);
    }
}
