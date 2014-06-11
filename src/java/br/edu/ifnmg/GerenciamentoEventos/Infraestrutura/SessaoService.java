/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Infraestrutura;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author petronio
 */
@Named
@RequestScoped
public class SessaoService {

    public void put(String key, String value) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        Cookie ck = new Cookie(key, value);
        ck.setMaxAge(-1);
        ((HttpServletResponse) ctx.getExternalContext().getResponse()).addCookie(ck);

    }

    public String get(String key) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) ctx.getExternalContext().getRequest();
        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {
            if (cookie.getName().trim().equalsIgnoreCase(key)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public void delete(String key) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) ctx.getExternalContext().getRequest();
        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {
            if (cookie.getName().trim().equalsIgnoreCase(key)) {
                cookie.setMaxAge(0);
                ((HttpServletResponse) ctx.getExternalContext().getResponse()).addCookie(cookie);
                break;
            }
        }
    }

    public void limpar() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) ctx.getExternalContext().getRequest();
        Cookie[] cookies = request.getCookies();

        for (Cookie cookie : cookies) {
            cookie.setMaxAge(0);
            ((HttpServletResponse) ctx.getExternalContext().getResponse()).addCookie(cookie);

        }
    }
}
