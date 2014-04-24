/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Presentation.Comum;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Permissao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Pessoa;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PermissaoRepositorio;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
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

    @EJB
    PermissaoRepositorio permissaoDAO;

    @Override
    public void afterPhase(PhaseEvent event) {

        FacesContext fc = event.getFacesContext();
        ExternalContext ec = fc.getExternalContext();
        String viewid = fc.getViewRoot().getViewId();
        if (!viewid.contains("login.xhtml") && !viewid.contains("cadastrar.xhtml") ) {
            HttpSession session = (HttpSession) ec.getSession(true);
            Pessoa usuario = (Pessoa) session.getAttribute("usuarioAutenticado");

            if (usuario == null) {
                try {
                    ec.redirect( ec.getApplicationContextPath() + "/login.xhtml");
                } catch (IOException ex) {
                    Logger.getLogger(AutenticacaoPhaseListener.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if(viewid.contains("/admin/")) {
                String tmp = viewid.substring(1);
                Permissao p = permissaoDAO.Abrir(tmp);
                if (!usuario.getPerfil().contains(p)) {
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
