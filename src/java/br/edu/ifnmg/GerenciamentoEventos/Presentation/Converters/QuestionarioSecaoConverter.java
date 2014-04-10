/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Presentation.Converters;


import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Questionario;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.QuestionarioSecao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.QuestionarioRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Presentation.Comum.GenericConverter;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.inject.Named;

/**
 *
 * @author petronio
 */
@Named(value = "questionarioSecaoConverter")
@SessionScoped
public class QuestionarioSecaoConverter  extends GenericConverter<Questionario, QuestionarioRepositorio>
        implements Serializable {

    @EJB
    QuestionarioRepositorio dao;

    @PostConstruct
    public void init() {
        setRepositorio(dao);
    }
 
    
     @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
         if (value.trim().equals("") || value == null) {  
            return null;  
        } else {  
            try {  
                long id = Long.parseLong(value);  
                QuestionarioSecao obj = dao.AbrirSecao(id);
                
                return obj;
  
            } catch(NumberFormatException exception) {  
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid player"));  
            } catch (Exception e){
                return null;
            } 
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null || value.equals("")) {  
            return "";  
        } else {  
            return String.valueOf(((QuestionarioSecao) value).getId());  
        }
    }
}
