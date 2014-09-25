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
package br.edu.ifnmg.GerenciamentoEventos.Infraestrutura;

import java.io.Serializable;
import java.util.HashMap;
import javax.ejb.Stateless;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author petronio
 */
@SessionScoped
public class SessaoService implements Serializable {

    public void put(String key, String value) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        Cookie ck = new Cookie(key, value);
        ck.setMaxAge(-1);
        ((HttpServletResponse) ctx.getExternalContext().getResponse()).addCookie(ck);
        cookies.put(key, ck);
    }

    HashMap<String, Cookie> cookies = new HashMap<>();

    private Cookie getCookie(String key) {
        if (cookies.isEmpty()) {
            FacesContext ctx = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) ctx.getExternalContext().getRequest();
            Cookie[] tmp = request.getCookies();

            for (Cookie cookie : tmp) {
                cookies.put(cookie.getName(), cookie);
            }
        }

        if (cookies.containsKey(key)) {
            return cookies.get(key);
        } else {
            return null;
        }
    }

    public String get(String key) {
        Cookie tmp = getCookie(key);
        if (tmp != null) {
            return tmp.getValue();
        }
        return null;
    }

    public void delete(String key) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        Cookie cookie = getCookie(key);
       
        cookies.remove(key);
        
        if (cookie != null) {
            cookie.setMaxAge(0);
            cookie.setValue(null);
            ((HttpServletResponse) ctx.getExternalContext().getResponse()).addCookie(cookie);
        }

    }

    public void limpar() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) ctx.getExternalContext().getRequest();
        Cookie[] cks = request.getCookies();

        for (Cookie cookie : cks) {
            cookie.setMaxAge(0);
            cookie.setValue(null);
            ((HttpServletResponse) ctx.getExternalContext().getResponse()).addCookie(cookie);

        }
    }
}
