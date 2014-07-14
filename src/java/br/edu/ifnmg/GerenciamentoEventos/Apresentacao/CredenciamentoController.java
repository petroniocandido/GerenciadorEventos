/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao;

import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBase;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Inscricao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.InscricaoItem;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.InscricaoStatus;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.EventoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.InscricaoConfirmacaoService;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.InscricaoRepositorio;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author petronio
 */
@Named(value = "credenciamentoController")
@RequestScoped
public class CredenciamentoController
        extends ControllerBase
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public CredenciamentoController() {
        
    }

    @EJB
    InscricaoRepositorio dao;
    
    @EJB
    EventoRepositorio daoEvt;
    
    @EJB
    InscricaoConfirmacaoService serv;
    
    Long id;
    
    Inscricao inscricao;
    
    Evento evento;
    
    public String buscar(){
        inscricao = dao.Abrir(getId());
        return "credenciamentoConfirmacao.xhtml";
    }
    
     public List<Inscricao> autoCompletar(String query) {
        return dao.Buscar(getEvento(), query);
    }
    
    public void credenciar(){
        if(getInscricao() == null){
            MensagemErro("ERRO", "Inscrição não encontrada!");
            return;
        } 
        
        if(serv.confirmar(getInscricao(), getUsuarioCorrente())){
            Mensagem("Sucesso!", "Inscrição confirmada com êxito!");
        }
    }
    
     public void checaEventoPadrao() {
        String evt = getConfiguracao("EVENTO_PADRAO");
        if (evt != null) {
            setSessao("credEvento", evt);
        }
    }

    public Long getId() {
        if(id == null){
            String tmp = getSessao("credInsc");
            if(tmp != null)
                id = Long.parseLong(tmp);
        }
        return id;
    }

    public void setId(Long id) {
        this.id = id;
        setSessao("credInsc", id.toString());
    }

    public Inscricao getInscricao() {
        if(inscricao == null){
            inscricao = (Inscricao)getSessao("credInsc", dao);
        }
        return inscricao;
    }

    public void setInscricao(Inscricao inscricao) {
        this.inscricao = inscricao;
        setSessao("credInsc", inscricao);
    }

    public Evento getEvento() {
        if(evento == null){
            evento = (Evento)getSessao("credEvento", daoEvt);
            if(evento == null){
                checaEventoPadrao();
                evento = (Evento)getSessao("credEvento", daoEvt);
            }
        }
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
        setSessao("credEvento", evento);
    }
    
    public void cancelar(InscricaoItem i){
        i.setStatus(InscricaoStatus.Cancelada);
        dao.Salvar(i.getInscricao());
    }

}
