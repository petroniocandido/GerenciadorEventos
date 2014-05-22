/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao.Converters;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Recurso;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.RecursoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.GenericConverter;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.RecursoTipo;
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
@Named(value = "recursoConverter")
@Singleton
public class RecursoConverter
        extends GenericConverter<Recurso, RecursoRepositorio>
        implements Serializable {

    @EJB
    RecursoRepositorio dao;

    @PostConstruct
    public void init() {
        setRepositorio(dao);
    }
    
    public List<Recurso> autoCompleteRecurso(String query) {
        Recurso i = new Recurso();
        i.setNome(query);
        return dao.Buscar(i);
    }
    
    public List<Recurso> autoCompleteImoveis(String query) {
        Recurso i = new Recurso();
        i.setNome(query);
        i.setTipo(RecursoTipo.Imovel);
        return dao.Buscar(i);
    }
}
