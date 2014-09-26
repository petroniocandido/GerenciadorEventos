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
    public AtividadeTipo getEntidade() {
        if (entidade == null) {
            Long tmp = getId();
            if(tmp == 0L)
                entidade = new AtividadeTipo();
            else 
                entidade = dao.AbrirTipo(tmp);
        }
        return entidade;
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
    
    @Override
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
        return dao.BuscarTipo(getFiltro());
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
