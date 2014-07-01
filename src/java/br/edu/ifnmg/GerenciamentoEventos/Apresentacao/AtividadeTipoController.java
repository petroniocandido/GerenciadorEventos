/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.*;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseEntidade;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.GenericDataModel;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.AtividadeRepositorio;
import java.io.IOException;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author petronio
 */
@Named(value = "atividadeTipoController")
@RequestScoped
public class AtividadeTipoController
        extends ControllerBaseEntidade<AtividadeTipo>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public AtividadeTipoController() {
    }

    @EJB
    AtividadeRepositorio dao;

    @PostConstruct
    public void init() {
        setRepositorio(dao);
        setPaginaEdicao("editarAtividadeTipo.xhtml");
        setPaginaListagem("listagemAtividadeTipos.xtml");
    }

    @Override
    public AtividadeTipo getFiltro() {
        if (filtro == null) {
            filtro = new AtividadeTipo();
            filtro.setNome(getSessao("atctrl_nome"));
        }
        return filtro;
    }

    @Override
    public void setFiltro(AtividadeTipo filtro) {
        this.filtro = filtro;
        if (filtro != null) {
            setSessao("atctrl_nome", filtro.getNome());
        }

    }

    @Override
    public void salvar() {
        Rastrear(getEntidade());

        // salva o objeto no BD
        if (dao.SalvarTipo(entidade)) {

            setId(entidade.getId());

            Mensagem("Sucesso", "Registro salvo com sucesso!");
            AppendLog("Editou a entidade " + entidade.getClass().getSimpleName() + " " + entidade.getId() + "(" + entidade.toString() + ")");
        } else {
            MensagemErro("Falha", "Registro não foi salvo! Consulte o Log ou o administrador do sistema!");
            AppendLog("Falha ao editar a entidade " + entidade.getClass().getSimpleName() + " " + entidade.getId() + "(" + entidade.toString() + ")" + ": " + repositorio.getErro().getMessage());
        }
        // atualiza a listagem
        filtrar();
    }

    @Override
    public String abrir() {
        setEntidade(dao.AbrirTipo(getId()));
        return "editarAtividadeTipo.xhtml";
    }
    
    public String apagar() {
        dao.ApagarTipo(getEntidade());
        filtrar();
        return "listagemAtividadeTipos.xtml";
    }

    @Override
    public void limpar() {
        setEntidade(new AtividadeTipo());
    }

    @Override
    public GenericDataModel getDataModel() {
        AtividadeTipoDataModel dm = new AtividadeTipoDataModel(getListagem(), null);
        dm.setAtividadeRepositorio(dao);
        return dm;
    }

    @Override
    public List<AtividadeTipo> getListagem() {
        return dao.BuscarTipo(null);
    }


    @Override
    public void onRowSelect(SelectEvent event) {
        try {
            setEntidade((AtividadeTipo) event.getObject());
            FacesContext.getCurrentInstance().getExternalContext().redirect(abrir());
        } catch (IOException ex) {

        }
    }

}
