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

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Pessoa;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.AutorizacaoService;
import br.edu.ifnmg.GerenciamentoEventos.Infraestrutura.AutenticacaoService;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.inject.Inject;

/**
 *
 * @author petronio
 */
public class AutenticacaoPhaseListener implements PhaseListener {

    @Inject
    AutenticacaoService autenticacao;
    
    @Inject
    AutorizacaoService autorizacao;            

    @Override
    public void afterPhase(PhaseEvent event) {

        FacesContext fc = event.getFacesContext();
        ExternalContext ec = fc.getExternalContext();
        if(fc.getViewRoot() == null)
            return;
        
        String viewid = fc.getViewRoot().getViewId();
        if (viewid.contains("/admin/") || viewid.contains("/publico/")) {
            
            Pessoa usuario = autenticacao.getUsuarioCorrente();

            if (usuario == null) {
                try {
                    ec.redirect( ec.getApplicationContextPath() + "/login.xhtml");
                } catch (IOException ex) {
                    Logger.getLogger(AutenticacaoPhaseListener.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if(viewid.contains("/admin/")) {
                String tmp = viewid.substring(1);
                if (!autorizacao.possuiPermissao(tmp)) {
                    try {
                        ec.redirect(ec.getApplicationContextPath() + "/" + usuario.getPerfil().getHome().getUri());
                    } catch (IOException ex) {
                        Logger.getLogger(AutenticacaoPhaseListener.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    @Override
    public void beforePhase(PhaseEvent event) {
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
}
