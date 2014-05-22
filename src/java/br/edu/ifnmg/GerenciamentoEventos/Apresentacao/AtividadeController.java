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
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author petronio
 */
@Named(value = "atividadeController")
@SessionScoped
public class AtividadeController
        extends ControllerBaseEntidade<Atividade>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public AtividadeController() {
        id = 0L;
        setEntidade(new Atividade());
        setFiltro(new Atividade());
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
    }

    public void checaEventoPadrao() {
        String evt = getConfiguracao("EVENTO_PADRAO");
        if (evt != null && padrao == null) {
            padrao = evtDAO.Abrir(Long.parseLong(evt));
            if(getEntidade().getEvento() == null)
                getEntidade().setEvento(padrao);
            if(getFiltro().getEvento() == null)
                getFiltro().setEvento(padrao);
        }
    }


    @Override
    public void filtrar() {
        checaEventoPadrao();
    }

    @Override
    public void salvar() {

        SalvarEntidade();

        // atualiza a listagem
        filtrar();
    }

    @Override
    public String apagar() {
        ApagarEntidade();
        filtrar();
        return "listagemAtividades.xtml";
    }

    @Override
    public String abrir() {
        setEntidade(dao.Abrir(id));
        return "editarAtividade.xhtml";
    }

    @Override
    public String cancelar() {
        return "listagemAtividades.xhtml";
    }

    @Override
    public void limpar() {
        checaEventoPadrao();
        setEntidade(new Atividade());
        getFiltro().setStatus(null);
    }

    @Override
    public String novo() {
        limpar();
        return "editarAtividade.xhtml";
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
        entidade.add(responsavel);
        SalvarAgregado(responsavel);
        responsavel = new Pessoa();
    }

    public void removeResponsavel() {
        entidade.remove(responsavel);
        RemoverAgregado(responsavel);
        responsavel = new Pessoa();
    }

    public void addAlocacao() {
        entidade = dao.Refresh(entidade);
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

}
