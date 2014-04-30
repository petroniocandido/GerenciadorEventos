/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao.Converters;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Pessoa;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PessoaRepositorio;
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
@Named(value = "usuarioConverter")
@RequestScoped
public class PessoaConverter
        extends GenericConverter<Pessoa, PessoaRepositorio>
        implements Serializable {

    @EJB
    PessoaRepositorio dao;

    @PostConstruct
    public void init() {
        setRepositorio(dao);
    }
}
