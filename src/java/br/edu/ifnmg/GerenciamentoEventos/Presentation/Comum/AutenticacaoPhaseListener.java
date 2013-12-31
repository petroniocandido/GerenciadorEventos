/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Presentation.Comum;


import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Pessoa;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpSession;

/**
 *
 * @author petronio
 */
public class AutenticacaoPhaseListener implements PhaseListener {

    @Override
    public void afterPhase(PhaseEvent event) {
        
        FacesContext fc = event.getFacesContext();
        ExternalContext ec = fc.getExternalContext();
        String viewid = fc.getViewRoot().getViewId();
        if (!viewid.contains("login.xhtml")) {
            HttpSession session = (HttpSession) ec.getSession(true);
            Pessoa usuario = (Pessoa) session.getAttribute("usuarioAutenticado");

            if (usuario == null) {
                try {
                    ec.redirect(ec.getRequestContextPath() + "/login.xhtml");
                } catch (IOException ex) {
                    Logger.getLogger(AutenticacaoPhaseListener.class.getName()).log(Level.SEVERE, null, ex);
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
