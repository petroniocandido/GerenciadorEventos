/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao.Converters;


import br.edu.ifnmg.DomainModel.AreaConhecimento;
import br.edu.ifnmg.DomainModel.Services.AreaConhecimentoRepositorio;
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
@Named(value = "areaConhecimentoConverter")
@Singleton
public class AreaConhecimentoConverter
        extends GenericConverter<AreaConhecimento, AreaConhecimentoRepositorio>
        implements Serializable {

    @EJB
    AreaConhecimentoRepositorio dao;

    List<AreaConhecimento> areas;
    
    @PostConstruct
    public void init() {
        setRepositorio(dao);
    }
    
    public List<AreaConhecimento> autoCompleteAreaConhecimento(String query) {
       return dao.BuscarTexto(query);
    }
    
    public List<AreaConhecimento> getAreas() {
        if(areas == null)
            areas = dao.Areas();
       return areas;
    }
}
