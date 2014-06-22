/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Aplicacao;



import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Entidade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Pessoa;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.AutenticacaoService;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.ConfiguracaoService;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.LogService;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.MailService;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.Repositorio;
import br.edu.ifnmg.GerenciamentoEventos.Infraestrutura.SessaoService;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 * @author petronio
 */
public abstract class ControllerBase {
        
    @EJB
    ConfiguracaoService configuracao;
    
    @Inject
    AutenticacaoService autentitacao;
    
    @Inject
    LogService log;
    
    @EJB
    MailService mail;    
    
    @Inject
    SessaoService sessao;
    
    String assunto, mensagem;
    
    List<Pessoa> destinatarios;
    
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
        log.Append(desc);
    }
    
    protected void Redirect(String url){
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect( url);
        } catch (IOException ex) {
            Logger.getLogger(ControllerBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    public void setConfiguracao(String chave, String valor) {
        configuracao.set(chave, valor);
        AppendLog("Alterando configuração global" + chave + " = " + valor);
    }

    public void setConfiguracao(Pessoa usr, String chave, String valor) {
        configuracao.setLocal(chave, valor);
        AppendLog("Alterando configuração de usuário " + chave + " = " + valor);
    }

    public String getConfiguracao(String chave) {
        return configuracao.get(chave);
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public List<Pessoa> getDestinatarios() {
        return destinatarios;
    }

    public void setDestinatarios(List<Pessoa> destinatarios) {
        this.destinatarios = destinatarios;
    }
    
    public void enviarMensagem() {
        int sucesso = 0, erro = 0;
        String erros = "";
        for(Pessoa p : destinatarios){
            if(mail.enviar(p.getEmail(), assunto, mensagem)){
                sucesso++;
            } else {
                erro++;
                erros += p.getEmail() + ",";
            }
        }
        Mensagem("Confirmação", sucesso +" e-mails enviados com sucesso!\n" + erro + " enviados com falha:\n " + erros);
    }
    
    public void setSessao(String key, Entidade obj) {
        if(sessao == null) return;
        
        if (obj != null && obj.getId() != null) {
            sessao.put(key, obj.getId().toString());
        } else {
            sessao.delete(key);
        }
    }
    
    public void setSessao(String key, String obj) {
        if(sessao == null) return;
        
        if (obj != null) {
            sessao.put(key, obj);
        } else {
            sessao.delete(key);
        }
    }

    public Entidade getSessao(String key, Repositorio dao) {
        if(sessao == null) return null;
        
        String tmp = sessao.get(key);
        if (tmp != null && !tmp.isEmpty()) {
            return (Entidade) dao.Abrir(Long.parseLong(tmp));
        }
        return null;
    }
    
    public String getSessao(String key) {
        if(sessao == null) return null;
        
        return sessao.get(key);
    }
    
}
