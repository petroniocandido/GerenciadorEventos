/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Presentation;

import br.edu.ifnmg.GerenciamentoEventos.DataAccess.PessoaDAO;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Atividade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Inscricao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Pessoa;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.AtividadeRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.EventoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.InscricaoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PessoaRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Status;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedProperty;

/**
 *
 * @author petronio
 */
@Named(value = "publicoController")
@RequestScoped
public class PublicoController {

    @ManagedProperty(value = "#{param.pessoaID}")
    Long pessoaID;
    @ManagedProperty(value = "#{param.eventoID}")
    Long eventoID;
    @ManagedProperty(value = "#{param.inscricaoID}")
    Long inscricaoID;

    @EJB
    EventoRepositorio eventoDAO;

    @EJB
    AtividadeRepositorio atividadeDAO;

    @EJB
    InscricaoRepositorio inscricaoDAO;

    @EJB
    PessoaRepositorio pessoaDAO;

    /**
     * Creates a new instance of PublicoController
     */
    public PublicoController() {

    }

    public List<Evento> getEventos() {
        Evento filtro = new Evento();
        filtro.setStatus(Status.EmExecucao);
        return eventoDAO.Buscar(filtro);
    }

    public List<Atividade> getAtividades() {
        if (eventoID > 0) {
            Atividade filtro = new Atividade();
            filtro.setEvento(eventoDAO.Abrir(eventoID));
            return atividadeDAO.Buscar(filtro);
        } else {
            return null;
        }
    }
    
    Inscricao inscricao;

    public Inscricao getInscricao() {
        if(inscricao == null){
        if (inscricaoID != null) {
            inscricao = inscricaoDAO.Abrir(inscricaoID);
            return inscricao;
        } else if (eventoID != null) {
            inscricao = inscricaoDAO.Abrir(getEvento(), getPessoa());
            return inscricao;
        } else {
            return null;
        }
        } else return inscricao;
    }

    Pessoa pessoa;

    public Pessoa getPessoa() {
        if (pessoa == null) {
            if (pessoaID != null) {
                pessoa = pessoaDAO.Abrir(pessoaID);
                return pessoa;
            } else {
                return null;
            }
        } else {
            return pessoa;
        }
    }

    public Long getPessoaID() {
        return pessoaID;
    }

    public void setPessoaID(Long pessoaID) {
        this.pessoaID = pessoaID;
    }

    Evento evento;

    public Evento getEvento() {
        if (evento == null) {
            if (eventoID != null) {
                evento = eventoDAO.Abrir(eventoID);
                return evento;
            } else {
                return null;
            }
        } else {
            return evento;
        }
    }

    public Long getEventoID() {
        return eventoID;
    }

    public void setEventoID(Long eventoID) {
        this.eventoID = eventoID;
    }

    public Long getInscricaoID() {
        return inscricaoID;
    }

    public void setInscricaoID(Long inscricaoID) {
        this.inscricaoID = inscricaoID;
    }

    public String selecionaEvento() {
        return "inscricao.xhtml";
    }

}
