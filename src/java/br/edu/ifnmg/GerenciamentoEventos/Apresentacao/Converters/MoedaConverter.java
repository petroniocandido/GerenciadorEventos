/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao.Converters;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
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
@Named(value = "moedaConverter")
@Singleton
public class MoedaConverter implements Converter {
    
    static Locale ptBR = new Locale("pt","BR");

    @Override
    public Object getAsObject(FacesContext facesContext,
            UIComponent uiComponent, String value) {

        FacesContext fc = FacesContext.getCurrentInstance();
        Locale l = fc.getViewRoot().getLocale();

        if (value != null) {
            value = value.trim();
            if (value.length() > 0) {
                try {
                    return new BigDecimal(NumberFormat.
                            getCurrencyInstance(ptBR).parse(
                                    value).doubleValue());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override

    public String getAsString(FacesContext facesContext,
            UIComponent uiComponent, Object value) {

        if (value == null) {
            return "";
        }
        if (value instanceof String) {

            return (String) value;
        }
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            Locale l = fc.getViewRoot().getLocale();
            NumberFormat formatador = NumberFormat.getCurrencyInstance(ptBR);

            formatador.setGroupingUsed(true);
            return formatador.format(value);

        } catch (Exception e) {
            throw new ConverterException("Formato não é número.");
        }
    }
}
