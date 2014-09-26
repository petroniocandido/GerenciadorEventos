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
package br.edu.ifnmg.GerenciamentoEventos.Aplicacao;


import br.edu.ifnmg.DomainModel.Entidade;
import br.edu.ifnmg.DomainModel.Services.Repositorio;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author petronio
 * @param <TObj>
 * @param <TDAO>
 */
public class GenericConverter<TObj extends Entidade, TDAO extends Repositorio<TObj>> implements Converter {

    private TDAO repositorio;

    protected void setRepositorio(TDAO repositorio) {
        this.repositorio = repositorio;
    }
    

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value.trim().equals("") || value == null) {
            return null;
        } else {
            try {
                long id = Long.parseLong(value);
                TObj obj = repositorio.Abrir(id);

                return obj;

            } catch (NumberFormatException exception) {
                //throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid player"));  
                return null;
            } catch (Exception e) {
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
                return String.valueOf(((TObj) value).getId());
            }
        } catch (Exception ex) {
            return "";
        }
    }
}
