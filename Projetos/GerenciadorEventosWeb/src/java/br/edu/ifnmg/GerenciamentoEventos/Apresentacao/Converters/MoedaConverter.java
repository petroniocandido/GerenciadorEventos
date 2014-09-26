/*
 *   This file is part of SGEA - Sistema de Gestão de Eventos Acadêmicos - TADS IFNMG Campus Januária.
 *
 *   SGEA is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   SGEA is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with SGEA.  If not, see <http://www.gnu.org/licenses/>.
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
