/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao.Converters;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.LancamentoCategoria;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.LancamentoRepositorio;
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
@Named(value = "lancamentoCategoriaConverter")
@Singleton
public class LancamentoCategoriaConverter implements Converter, Serializable {

    @EJB
    private LancamentoRepositorio repositorio;

     public List<LancamentoCategoria> autoCompleteLancamentoCategoria(String query) {
        LancamentoCategoria i = new LancamentoCategoria();
        i.setNome(query);
        return repositorio.BuscarCategorias(i);
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().equals("") ) {
            return null;
        } else {
            try {
                long id = Long.parseLong(value);
                LancamentoCategoria obj = repositorio.AbrirCategoria(id);

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
                return String.valueOf(((LancamentoCategoria) value).getId());
            }
        } catch (Exception ex) {
            return "";
        }
    }
}
