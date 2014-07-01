/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Alocacao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Atividade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Pessoa;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.AtividadeRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.EventoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Status;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseEntidade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.AlocacaoStatus;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.AtividadeTipo;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author petronio
 */
@Named(value = "atividadeController")
@RequestScoped
public class AtividadeController
        extends ControllerBaseEntidade<Atividade>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public AtividadeController() {
        getFiltro().setStatus(null);
        alocacao = new Alocacao();
        responsavel = new Pessoa();
    }

    Evento padrao;

    @EJB
    AtividadeRepositorio dao;

    @EJB
    EventoRepositorio evtDAO;

    Pessoa responsavel;

    Alocacao alocacao;

    @PostConstruct
    public void init() {
        setRepositorio(dao);
        checaEventoPadrao();
        setPaginaEdicao("editarAtividade.xhtml");
        setPaginaListagem("listagemAtividades.xtml");
    }
    
    @Override
    public Atividade getFiltro() {
        if (filtro == null) {
            filtro = new Atividade();
            filtro.setNome(getSessao("atctrl_nome"));
            filtro.setEvento((Evento)getSessao("atctrl_evt",evtDAO));
            String tmp = getSessao("atctrl_sit");
            filtro.setStatus(tmp != null ? Status.valueOf(tmp) : null);
        }
        return filtro;
    }

    @Override
    public void setFiltro(Atividade filtro) {
        this.filtro = filtro;
        if (filtro != null) {
            setSessao("atctrl_nome", filtro.getNome());
            setSessao("atctrl_evt", filtro.getEvento());
            setSessao("atctrl_sit", filtro.getStatus() != null ? filtro.getStatus().name() : null);
        }

    }


    public void checaEventoPadrao() {
        String evt = getConfiguracao("EVENTO_PADRAO");
        if (evt != null) {
            padrao = evtDAO.Abrir(Long.parseLong(evt));
            if (getEntidade().getEvento() == null) {
                getEntidade().setEvento(padrao);
            }
            if (getFiltro().getEvento() == null) {
                getFiltro().setEvento(padrao);
            }
        }
    }

    @Override
    public void filtrar() {
        checaEventoPadrao();
        setFiltro(filtro);
    }


    @Override
    public void limpar() {
        setEntidade(new Atividade());
        getFiltro().setStatus(null);
        checaEventoPadrao();
    }


    public Status[] getStatus() {
        return Status.values();
    }

    public Pessoa getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Pessoa responsavel) {
        this.responsavel = responsavel;
    }

    public Alocacao getAlocacao() {
        return alocacao;
    }

    public void setAlocacao(Alocacao alocacao) {
        this.alocacao = alocacao;
    }

    public void addResponsavel() {
        entidade = dao.Refresh(getEntidade());
        entidade.add(responsavel);
        SalvarAgregado(responsavel);
        responsavel = new Pessoa();
    }

    public void removeResponsavel() {
        entidade = dao.Refresh(getEntidade());
        entidade.remove(responsavel);
        RemoverAgregado(responsavel);
        responsavel = new Pessoa();
    }

    public void addAlocacao() {
        entidade = dao.Refresh(getEntidade());
        Rastrear(alocacao);
        alocacao.setInicio(entidade.getInicio());
        alocacao.setTermino(entidade.getTermino());
        entidade.add(alocacao);
        SalvarAgregado(alocacao);
        alocacao = new Alocacao();
    }

    public void removeAlocacao() {
        entidade.remove(alocacao);
        RemoverAgregado(alocacao);
        alocacao = new Alocacao();
    }

    public String getCorStatus(Atividade a) {
        if (a == null) {
            return "white";
        }

        Date hoje = new Date();

        if (a.getInicio().before(hoje)) {
            switch (a.getStatus()) {
                case EmExecucao:
                    return "green";
                case Pendente:
                    return "yellow";
                case Cancelado:
                    return "gray";
                case Concluido:
                    return "green";
            }
        }

        if (a.getInicio().before(hoje) && a.getTermino().after(hoje)) {
            switch (a.getStatus()) {
                case EmExecucao:
                    return "green";
                case Pendente:
                    return "red";
                case Cancelado:
                    return "gray";
                case Concluido:
                    return "green";
            }
        }

        if (a.getTermino().before(hoje)) {
            switch (a.getStatus()) {
                case EmExecucao:
                    return "red";
                case Pendente:
                    return "red";
                case Cancelado:
                    return "gray";
                case Concluido:
                    return "green";
            }
        }

        return "white";

    }

    public AlocacaoStatus[] getStatusAlocacao() {
        return AlocacaoStatus.values();
    }
    
    public List<AtividadeTipo> getListagemTipos(){
        return dao.BuscarTipo(null);
    }

}
