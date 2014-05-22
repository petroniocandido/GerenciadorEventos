/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao.Converters;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Pessoa;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PessoaRepositorio;
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
@Named(value = "usuarioConverter")
@Singleton
public class PessoaConverter
        extends GenericConverter<Pessoa, PessoaRepositorio>
        implements Serializable {

    @EJB
    PessoaRepositorio dao;

    @PostConstruct
    public void init() {
        setRepositorio(dao);
    }
    
    public List<Pessoa> autoCompletePessoa(String query) {
        Pessoa i = new Pessoa();
        i.setNome(query);
        return dao.Buscar(i);
    }
}
