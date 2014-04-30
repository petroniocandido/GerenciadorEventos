/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao.Converters;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Arquivo;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.ArquivoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.GenericConverter;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author petronio
 */
@Named(value = "arquivoConverter")
@RequestScoped
public class ArquivoConverter
        extends GenericConverter<Arquivo, ArquivoRepositorio>
        implements Serializable {

    @EJB
    ArquivoRepositorio dao;

    @PostConstruct
    public void init() {
        setRepositorio(dao);
    }
}
