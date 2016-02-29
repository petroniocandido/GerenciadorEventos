/*
 *   This file is part of SGEA - Sistema de Gestão de Eventos Acadêmicos - TADS IFNMG Campus Januária.
 *
 *   SGEA is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   SGEA is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with SGEA.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Alocacao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Atividade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
import br.edu.ifnmg.DomainModel.Pessoa;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.AtividadeRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.EventoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Status;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseEntidade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.AlocacaoStatus;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.AtividadeTipo;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Recurso;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.AlocacaoRepositorio;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

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

        alocacao = new Alocacao();
        responsavel = new Pessoa();
    }

    Evento padrao;

    @EJB
    AtividadeRepositorio dao;

    @EJB
    EventoRepositorio evtDAO;

    @EJB
    AlocacaoRepositorio alocDAO;

    Pessoa responsavel;

    Alocacao alocacao;

    @PostConstruct
    public void init() {
        getFiltro().setStatus(null);
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
            filtro.setEvento((Evento) getSessao("atctrl_evt", evtDAO));
            String tip = getSessao("atctrl_tip");
            filtro.setTipo(tip != null ? dao.AbrirTipo(Long.parseLong(tip)) : null);
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
            setSessao("atctrl_tip", filtro.getTipo());
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

        List<Alocacao> tmp = alocDAO.conflitos(alocacao);

        if (!tmp.isEmpty()) {
            MensagemErro("Conflito de Horário", "O recurso já está alocado para o horário desta atividade!");
            return;
        }

        entidade.add(alocacao);
        SalvarAgregado(alocacao);
        alocacao = new Alocacao();
    }

    public void validaAlocacao(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        entidade = dao.Refresh(getEntidade());

        Alocacao tmp = new Alocacao();
        tmp.setRecurso((Recurso) value);
        tmp.setInicio(entidade.getInicio());
        tmp.setTermino(entidade.getTermino());

        List<Alocacao> conflitos = alocDAO.conflitos(tmp);

        if (!conflitos.isEmpty() && !conflitos.get(0).getAtividade().equals(entidade) ) {
             FacesMessage msg
                    = new FacesMessage("Conflito de Horário", "O recurso "+ value +" já está alocado para este horário na atividade " + conflitos.get(0).getAtividade() +"!");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
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

    public List<AtividadeTipo> getListagemTipos() {
        return dao.BuscarTipo(null);
    }

}
