/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao.Converters;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Inscricao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.InscricaoRepositorio;
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
@Named(value = "inscricaoConverter")
@Singleton
public class InscricaoConverter
        extends GenericConverter<Inscricao, InscricaoRepositorio>
        implements Serializable {

    @EJB
    InscricaoRepositorio dao;

    @PostConstruct
    public void init() {
        setRepositorio(dao);
    }
    
     public List<Inscricao> autoCompleteInscricao(String query) {
        Inscricao i = new Inscricao();
        Long id2 = Long.parseLong(query);
        i.setId(id2);
        return dao.Buscar(i);
    }
}
