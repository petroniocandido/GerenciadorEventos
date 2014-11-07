/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.ifnmg.GerenciamentoEventos.Apresentacao.Converters;

import java.math.BigDecimal;
import java.util.Locale;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 *
 * @author petronio
 */
@Named(value = "bigDecimalConverter")
@Singleton
public class BigDecimalConverter implements Converter {
    
    @Override
    public Object getAsObject(FacesContext facesContext,
            UIComponent uiComponent, String value) {

        FacesContext fc = FacesContext.getCurrentInstance();
        Locale l = fc.getViewRoot().getLocale();

        if (value != null) {
            value = value.trim();
            if (value.length() > 0) {
                return new BigDecimal (value.replace(",", ""));
            }
        }
        return null;
    }

    @Override

    public String getAsString(FacesContext facesContext,
            UIComponent uiComponent, Object value) {

        if (value == null) {
            return "0.00";
        }
        
        if(value instanceof BigDecimal )
            return ((BigDecimal) value).toString();
        
        throw new ConverterException("Formato não é número.");
        
    }
}
