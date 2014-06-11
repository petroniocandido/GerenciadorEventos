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
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.GenericDataModel;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.AlocacaoStatus;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.AtividadeTipo;
import java.io.IOException;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;

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
        tipo = new AtividadeTipo();
        filtroTipo = new AtividadeTipo();
    }

    Evento padrao;

    @EJB
    AtividadeRepositorio dao;

    @EJB
    EventoRepositorio evtDAO;

    Pessoa responsavel;

    Alocacao alocacao;

    AtividadeTipo tipo, filtroTipo;

    public AtividadeTipo getTipo() {
        return tipo;
    }

    public void setTipo(AtividadeTipo tipo) {
        this.tipo = tipo;
    }

    public AtividadeTipo getFiltroTipo() {
        return filtroTipo;
    }

    public void setFiltroTipo(AtividadeTipo filtroTipo) {
        this.filtroTipo = filtroTipo;
    }

    @PostConstruct
    public void init() {
        setRepositorio(dao);
        checaEventoPadrao();
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
        setEntidade(new Atividade());
        getFiltro().setStatus(null);
        checaEventoPadrao();
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
        entidade = dao.Refresh(entidade);
        entidade.add(responsavel);
        SalvarAgregado(responsavel);
        responsavel = new Pessoa();
    }

    public void removeResponsavel() {
        entidade = dao.Refresh(entidade);
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

    public void salvarTipo() {
        Rastrear(entidade);

        // salva o objeto no BD
        if (dao.SalvarTipo(tipo)) {

            setId(tipo.getId());

            Mensagem("Sucesso", "Registro salvo com sucesso!");
            AppendLog("Editou a entidade " + tipo.getClass().getSimpleName() + " " + tipo.getId() + "(" + tipo.toString() + ")");
        } else {
            MensagemErro("Falha", "Registro não foi salvo! Consulte o Log ou o administrador do sistema!");
            AppendLog("Falha ao editar a entidade " + tipo.getClass().getSimpleName() + " " + tipo.getId() + "(" + tipo.toString() + ")" + ": " + repositorio.getErro().getMessage());
        }
        // atualiza a listagem
        filtrar();
    }

    public String apagarTipo() {
        dao.ApagarTipo(tipo);
        filtrar();
        return "listagemAtividadeTipos.xtml";
    }

    public String abrirTipo() {
        //setTipo(dao.AbrirTipo(id));
        return "editarAtividadeTipo.xhtml";
    }

    public String cancelarTipo() {
        return "listagemAtividadeTipos.xhtml";
    }

    public void limparTipo() {

        setTipo(new AtividadeTipo());
    }

    public String novoTipo() {
        limpar();
        return "editarAtividadeTipo.xhtml";
    }

    public List<AtividadeTipo> getListagemTipos() {
        return dao.BuscarTipo(null);
    }

    public GenericDataModel getDataModelTipo() {
        AtividadeTipoDataModel dm = new AtividadeTipoDataModel(dao.BuscarTipo(filtroTipo), null);
        dm.setAtividadeRepositorio(dao);
        return dm;
    }

    public void onRowSelectTipo(SelectEvent event) {
        try {
            tipo = (AtividadeTipo) event.getObject();
            FacesContext.getCurrentInstance().getExternalContext().redirect(abrirTipo());
        } catch (IOException ex) {

        }
    }

}
