/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao;


import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseEntidade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.AtividadeTipo;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.EventoInscricaoCategoria;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.EventoInscricaoCategoriaRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.EventoRepositorio;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author Isla Guedes
 */
@Named(value = "eventoInscricaoCategoriaController")
@RequestScoped
public class EventoInscricaoCategoriaController
    extends ControllerBaseEntidade<EventoInscricaoCategoria> implements Serializable {
    
    AtividadeTipo atividadeTipo;

    Integer limite;

    /**
     * Creates a new instance of CampusController
     */
    public EventoInscricaoCategoriaController() {   
        padrao = new Evento();
    }
    
    @EJB
    EventoInscricaoCategoriaRepositorio dao;
    
    @EJB 
    EventoRepositorio daoE;
    
    Evento padrao;
    
   
    public void checaEventoPadrao() {
        String evt = getConfiguracao("EVENTO_PADRAO");
        if (evt != null) {
            padrao = daoE.Abrir(Long.parseLong(evt));
            if (getEntidade().getEvento() == null) {
                getEntidade().setEvento(padrao);
            }
            if (getFiltro().getEvento() == null) {
                getFiltro().setEvento(padrao);
            }
        }
    }
    
    @Override
    public EventoInscricaoCategoria getFiltro() {
        if (filtro == null) {
            filtro = new EventoInscricaoCategoria();
            filtro.setEvento((Evento)getSessao("eicctrl_evt",daoE));
        }
        return filtro;
    }

    @Override
    public void setFiltro(EventoInscricaoCategoria filtro) {
        this.filtro = filtro;
        if (filtro != null) {
            setSessao("eicctrl_evt", filtro.getNome());
        }
    }

    @PostConstruct
    public void init() {
        setRepositorio(dao);
        checaEventoPadrao();
        setPaginaEdicao("editarEventoInscricaoCategoria.xhtml");
        setPaginaListagem("listagemEventoInscricaoCategorias.xhtml");
    }
    
    @Override
    public void limpar() {
        setEntidade(new EventoInscricaoCategoria());
    }
    
    public void addLimite() {
        entidade = dao.Refresh(getEntidade());
        entidade.addLimite(atividadeTipo, limite);
        SalvarAgregado(atividadeTipo);
        atividadeTipo = new AtividadeTipo();
    }

    public void removeLimite() {
        entidade = dao.Refresh(getEntidade());
        entidade.removeLimite(atividadeTipo);
        RemoverAgregado(atividadeTipo);
        atividadeTipo = new AtividadeTipo();
    }
    
     public Integer getLimite() {
        return limite;
    }

    public void setLimite(Integer limite) {
        this.limite = limite;
    }

    public List<Map.Entry<AtividadeTipo, Integer>> getLimitesAtividades() {
        return new ArrayList<>(entidade.getInscricoesPorAtividade().entrySet());
    }

    public AtividadeTipo getAtividadeTipo() {
        return atividadeTipo;
    }

    public void setAtividadeTipo(AtividadeTipo atividadeTipo) {
        this.atividadeTipo = atividadeTipo;
    }

    public Evento getPadrao() {
        return padrao;
    }

    public void setPadrao(Evento padrao) {
        this.padrao = padrao;
    }
    
    
    
}

