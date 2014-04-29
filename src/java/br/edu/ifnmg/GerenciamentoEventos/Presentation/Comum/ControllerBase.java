/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Presentation.Comum;



import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Configuracao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Entidade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Log;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Permissao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Pessoa;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.AutenticacaoService;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.ConfiguracaoRepositorio;
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

/**
 *
 * @author petronio
 */
public abstract class ControllerBase {
    
    
    @EJB
    ConfiguracaoRepositorio confDAO;

    @EJB
    private PermissaoRepositorio permissaoDAO;
    
    @EJB
    private LogRepositorio logDAO;
    
    @EJB
    protected AutenticacaoService autentitacao;
    
    public Pessoa getUsuarioCorrente() {
        return autentitacao.getUsuarioCorrente();
    }

    protected void Mensagem(Severity severity, String titulo, String msg) {
        FacesContext context = FacesContext.getCurrentInstance();

        FacesMessage m = new FacesMessage(severity, msg, titulo);
        context.addMessage(null, m);
        context.getExternalContext().getFlash().setKeepMessages(true);
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
            obj.setCriador(autentitacao.getUsuarioCorrente());
        }

        obj.setDataUltimaAlteracao(new Date());
        obj.setUltimoAlterador(autentitacao.getUsuarioCorrente());
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
            log.setUsuario(autentitacao.getUsuarioCorrente());
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
    
    
    public void setConfiguracao(String chave, String valor) {
        confDAO.Set(chave, valor);
        AppendLog("Alterando configuração global" + chave + " = " + valor);
    }

    public void setConfiguracao(Pessoa usr, String chave, String valor) {
        confDAO.Set(usr, chave, valor);
        AppendLog("Alterando configuração de usuário " + chave + " = " + valor);
    }

    public String getConfiguracao(String chave) {
        Configuracao c = confDAO.Abrir(getUsuarioCorrente(), chave);
        if (c == null) {
<<<<<<< HEAD
            c = confDAO.Abrir(autentitacao.getUsuarioCorrente(), chave);
=======
            c = confDAO.Abrir(chave);
>>>>>>> master
        }
        if (c != null) {
            return c.getValor();
        } else {
            return null;
        }
    }
}
