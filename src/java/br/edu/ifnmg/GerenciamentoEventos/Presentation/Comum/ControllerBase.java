/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Presentation.Comum;



import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Entidade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Log;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Permissao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Pessoa;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.LogRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PermissaoRepositorio;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author petronio
 */
public abstract class ControllerBase {

    @EJB
    private PermissaoRepositorio permissaoDAO;
    
    @EJB
    private LogRepositorio logDAO;
    
    private Pessoa usuarioLogado;

    public Pessoa getUsuarioCorrente() {
        if (usuarioLogado == null) {
            HttpSession session;
            FacesContext ctx = FacesContext.getCurrentInstance();
            session = (HttpSession) ctx.getExternalContext().getSession(false);
            usuarioLogado = (Pessoa) session.getAttribute("usuarioAutenticado");
        }
        return usuarioLogado;
    }

    protected void Mensagem(Severity severity, String titulo, String msg) {
        FacesContext context = FacesContext.getCurrentInstance();

        FacesMessage m = new FacesMessage(severity, msg, titulo);
        context.addMessage(null, m);
    }

    protected void Mensagem(String titulo, String msg) {
        Mensagem(FacesMessage.SEVERITY_INFO, titulo, msg);
    }

    protected void MensagemErro(String titulo, String msg) {
        Mensagem(FacesMessage.SEVERITY_ERROR, titulo, msg);
    }

    protected void Rastrear(Entidade obj) {
        if (obj.getId() == null || obj.getId() == 0L) {
            obj.setDataCriacao(new Date());
            obj.setCriador(getUsuarioCorrente());
        }

        obj.setDataUltimaAlteracao(new Date());
        obj.setUltimoAlterador(getUsuarioCorrente());
    }

    protected void AppendLog(String desc) {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String ip = httpServletRequest.getRemoteHost();
            String page = FacesContext.getCurrentInstance().getViewRoot().getViewId();
            
            page = page.substring(1, page.length());
            
            Permissao p = permissaoDAO.Abrir(page);
            
            Log log = new Log();
            log.setDescricao(desc);
            log.setUsuario(getUsuarioCorrente());
            log.setPermissao(p);
            log.setMaquina(ip);

            logDAO.Salvar(log);
            
        } catch(Exception ex){
        } finally {
        }
    }
    
    protected void Redirect(String url){
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect( url);
        } catch (IOException ex) {
            Logger.getLogger(ControllerBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
