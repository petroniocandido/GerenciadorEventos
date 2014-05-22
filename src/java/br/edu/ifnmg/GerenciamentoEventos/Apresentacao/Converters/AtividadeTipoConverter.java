/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao.Converters;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.AtividadeTipo;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.AtividadeRepositorio;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 *
 * @author petronio
 */
@Named(value = "atividadeTipoConverter")
@Singleton
public class AtividadeTipoConverter implements Converter, Serializable {

    @EJB
    private AtividadeRepositorio repositorio;

     public List<AtividadeTipo> autoCompleteAtividadeTipo(String query) {
        AtividadeTipo i = new AtividadeTipo();
        i.setNome(query);
        return repositorio.BuscarTipo(i);
    }
    

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().equals("") ) {
            return null;
        } else {
            try {
                long id = Long.parseLong(value);
                AtividadeTipo obj = repositorio.AbrirTipo(id);

                return obj;

            } catch (NumberFormatException exception) {
                //throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid player"));  
                return null;
            } 
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {
            if (value == null || value.equals("")) {
                return "";
            } else {
                return String.valueOf(((AtividadeTipo) value).getId());
            }
        } catch (Exception ex) {
            return "";
        }
    }
}
