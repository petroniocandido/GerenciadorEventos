/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao.Converters;


import br.edu.ifnmg.DomainModel.Campus;
import br.edu.ifnmg.DomainModel.Services.CampusRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.GenericConverter;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Singleton;

/**
 *
 * @author Isla Guedes
 */
@Named(value = "campusConverter")
@Singleton
public class CampusConverter
        extends GenericConverter<Campus, CampusRepositorio>
        implements Serializable {

    @EJB
    CampusRepositorio dao;

    @PostConstruct
    public void init() {
        setRepositorio(dao);
    }
    
    public List<Campus> autoCompleteCampus(String query) {
        Campus i = new Campus();
        i.setNome(query);
        return dao.Buscar(i);
    }
}
