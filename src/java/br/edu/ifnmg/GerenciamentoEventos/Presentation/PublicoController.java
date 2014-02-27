/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Presentation;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Atividade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.AtividadeTipo;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Inscricao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Pessoa;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.AtividadeRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.EventoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.InscricaoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PessoaRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Status;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author petronio
 */
@Named(value = "publicoController")
@ConversationScoped
public class PublicoController implements Serializable {

    @EJB
    EventoRepositorio eventoDAO;

    @EJB
    AtividadeRepositorio atividadeDAO;

    @EJB
    InscricaoRepositorio inscricaoDAO;

    @EJB
    PessoaRepositorio pessoaDAO;
    
    Evento evento;
    
    Atividade atividade;

    /**
     * Creates a new instance of PublicoController
     */
    public PublicoController() {

    }

    List<Evento> eventos;

    public List<Evento> getEventos() {
        if (eventos == null) {
            Evento filtro = new Evento();
            filtro.setStatus(Status.EmExecucao);
            eventos = eventoDAO.Buscar(filtro);
        }
        return eventos;
    }

    List<AtividadeTipo> tipos = new ArrayList<>();

    public List<AtividadeTipo> getAtividadesTipos() {
        if (tipos == null) {
            for (Atividade a : getAtividades()) {
                if (!tipos.contains(a.getTipo())) {
                    tipos.add(a.getTipo());
                }
            }
        }
        return tipos;
    }

    List<Atividade> atividades;

    public List<Atividade> getAtividades() {
        if (atividades == null) {
            Atividade filtro = new Atividade();
                AtividadeTipo tipo = new AtividadeTipo();
                tipo.setPublico(true);
                filtro.setTipo(tipo);
                filtro.setEvento(getEvento());
                atividades = atividadeDAO.Buscar(filtro);
                return atividades;
            
        } else {
            return atividades;
        }
    }

    public List<Atividade> getAtividades(AtividadeTipo t) {
        List<Atividade> tmp = new ArrayList<>();
        for (Atividade a : getAtividades()) {
            if (a.getTipo().equals(t)) {
                tmp.add(a);
            }
        }
        return tmp;
    }

    Inscricao inscricao;

    public Inscricao getInscricao() {
        if (inscricao == null) {
            if (getEvento() != null) {
                inscricao = inscricaoDAO.Abrir(getEvento(), getPessoa());
                return inscricao;
            } else {
                return null;
            }
        } else {
            return inscricao;
        }
    }

    Pessoa pessoa;

    public Pessoa getPessoa() {
        if (pessoa == null) {
                FacesContext fc = FacesContext.getCurrentInstance();
                ExternalContext ec = fc.getExternalContext();
                HttpSession session = (HttpSession) ec.getSession(true);
                pessoa = (Pessoa) session.getAttribute("usuarioAutenticado");                
            }
                 
        return pessoa;        
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Atividade getAtividade() {
        return atividade;
    }

    public void setAtividade(Atividade atividade) {
        this.atividade = atividade;
    }

    

    public String selecionaEvento() {
        return "inscricao.xhtml";
    }

    public void inscreverEvento() {
        Inscricao i = new Inscricao();
        i.setEvento(getEvento());
        i.setPessoa(getPessoa());
        inscricaoDAO.Salvar(i);        
    }

}
