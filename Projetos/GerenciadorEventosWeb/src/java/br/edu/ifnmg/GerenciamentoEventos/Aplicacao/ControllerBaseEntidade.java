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
package br.edu.ifnmg.GerenciamentoEventos.Aplicacao;

import br.edu.ifnmg.DomainModel.Arquivo;
import br.edu.ifnmg.DomainModel.Entidade;
import br.edu.ifnmg.DomainModel.Services.ArquivoRepositorio;
import br.edu.ifnmg.DomainModel.Services.Repositorio;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author petronio
 * @param <T>
 */
public abstract class ControllerBaseEntidade<T extends Entidade> extends ControllerBase {

    protected Long id;
    
    private String DIRETORIO_ARQUIVOS;

    private String classe = "";
    private String paginaEdicao;
    private String paginaListagem;

    @EJB
    ArquivoRepositorio arqDAO;

    public String getDIRETORIO_ARQUIVOS() {
        if(DIRETORIO_ARQUIVOS == null)
            DIRETORIO_ARQUIVOS = getConfiguracao("DIRETORIO_ARQUIVOS");
        return DIRETORIO_ARQUIVOS;
    }

    
    
    public Long getId() {
        if (id == null || id == 0L) {
            String tmp = getSessao(classe + "entidade");
            id = (tmp != null) ? Long.parseLong(tmp) : 0L;
        }
        return id;
    }

    public void setId(Long id) {
        if (id != null || id != 0L) {
            setSessao(classe + "entidade", id.toString());
        }
        this.id = id;
    }
    
    public abstract void limpar();

    public void filtrar() {
        setFiltro(filtro);
    }

    public void salvar() {

        SalvarEntidade();

        // atualiza a listagem
        filtrar();
    }

    public String apagar() {
        ApagarEntidade();
        filtrar();
        return paginaListagem;
    }

    public String abrir() {
        setEntidade(repositorio.Abrir(getId()));
        return paginaEdicao;
    }

    public String cancelar() {
        return paginaListagem;
    }

    public String novo() {
        limpar();
        return paginaEdicao;
    }

    public List<T> getListagem() {
        return repositorio.Buscar(getFiltro());
    }

    protected T entidade, filtro;
    protected Repositorio<T> repositorio;

    public T getEntidade() {
        if (entidade == null) {
            entidade = (T) getSessaoNaoNula(classe + "entidade", repositorio);
        }
        return entidade;
    }

    public void setEntidade(T entidade) {
        this.entidade = entidade;
        setSessao(classe + "entidade", entidade);
    }

    public void setRepositorio(Repositorio repo) {
        this.repositorio = repo;
        classe = this.repositorio.getTipo().getSimpleName();
    }

    public T getFiltro() {
        return filtro;
    }

    public void setFiltro(T filtro) {
        this.filtro = filtro;
    }

    protected void SalvarEntidade() {
        Rastrear(getEntidade());

        // salva o objeto no BD
        if (repositorio.Salvar(entidade)) {

            setId(entidade.getId());

            Mensagem("Sucesso", "Registro salvo com sucesso!");
            AppendLog("Editou a entidade " + entidade.getClass().getSimpleName() + " " + entidade.getId() + "(" + entidade.toString() + ")");
        } else {
            MensagemErro("Falha", "Registro não foi salvo! Consulte o Log ou o administrador do sistema!");
            AppendLog("Falha ao editar a entidade " + entidade.getClass().getSimpleName() + " " + entidade.getId() + "(" + entidade.toString() + ")" + ": " + repositorio.getErro().getMessage());
        }
    }

    protected void ApagarEntidade() {
        Rastrear(getEntidade());

        // salva o objeto no BD
        if (repositorio.Apagar(entidade)) {

            Mensagem("Sucesso", "Registro removido com sucesso!");
            AppendLog("Apagou a entidade " + entidade.getClass().getSimpleName() + " " + entidade.getId() + "(" + entidade.toString() + ")");
        } else {
            MensagemErro("Falha", "Registro não foi removido! Consulte o Log ou o administrador do sistema!");
            AppendLog("Falha ao remover a entidade " + entidade.getClass().getSimpleName() + " " + entidade.getId() + "(" + entidade.toString() + ")" + ": " + repositorio.getErro().getMessage());
        }
    }

    protected void SalvarAgregado(Entidade obj) {
        Rastrear(getEntidade());
        if (repositorio.Salvar(entidade)) {
            Mensagem("Sucesso", "Item adicionado com sucesso!");
            AppendLog("Adicionando " + obj.getClass().getSimpleName() + " " + obj.getId() + "(" + obj.toString() + ") à entidade " + entidade.getClass().getSimpleName() + " " + entidade.getId() + "(" + entidade.toString() + ")");
        } else {
            AppendLog("Falha ao adicionar " + obj.getClass().getSimpleName() + " " + obj.getId() + "(" + obj.toString() + ") à entidade " + entidade.getClass().getSimpleName() + " " + entidade.getId() + "(" + entidade.toString() + ")"
                    + ": " + repositorio.getErro().getMessage());
            MensagemErro("Falha", "Item não adicionado! Consulte o log.");
        }
    }

    protected void SalvarAgregado(Object obj) {
        Rastrear(getEntidade());
        if (repositorio.Salvar(entidade)) {
            Mensagem("Sucesso", "Item adicionado com sucesso!");
            AppendLog("Adicionando " + obj.toString() + " à entidade " + entidade.getClass().getSimpleName() + " " + entidade.getId() + "(" + entidade.toString() + ")");
        } else {
            AppendLog("Falha ao adicionar " + obj.toString() + " à entidade " + entidade.getClass().getSimpleName() + " " + entidade.getId() + "(" + entidade.toString() + ")"
                    + ": " + repositorio.getErro().getMessage());
            MensagemErro("Falha", "Item não adicionado! Consulte o log.");
        }
    }

    protected void RemoverAgregado(Entidade obj) {
        Rastrear(getEntidade());
        if (repositorio.Salvar(entidade)) {
            Mensagem("Sucesso", "Item removido com sucesso!");
            AppendLog("Removendo " + obj.getClass().getSimpleName() + " " + obj.getId() + "(" + obj.toString() + ") à entidade " + entidade.getClass().getSimpleName() + " " + entidade.getId() + "(" + entidade.toString() + ")");
        } else {
            AppendLog("Falha ao remover " + obj.getClass().getSimpleName() + " " + obj.getId() + "(" + obj.toString() + ") à entidade " + entidade.getClass().getSimpleName() + " " + entidade.getId() + "(" + entidade.toString() + ")"
                    + ": " + repositorio.getErro().getMessage());
            MensagemErro("Falha", "Item não removido! Consulte o log.");
        }
    }

    protected void RemoverAgregado(Object obj) {
        Rastrear(getEntidade());
        if (repositorio.Salvar(getEntidade())) {
            Mensagem("Sucesso", "Item removido com sucesso!");
            AppendLog("Removendo " + obj.toString() + " à entidade " + entidade.getClass().getSimpleName() + " " + entidade.getId() + "(" + entidade.toString() + ")");
        } else {
            AppendLog("Falha ao remover " + obj.toString() + " da entidade " + entidade.getClass().getSimpleName() + " " + entidade.getId() + "(" + entidade.toString() + ")"
                    + ": " + repositorio.getErro().getMessage());
            MensagemErro("Falha", "Item não removido! Consulte o log.");
        }
    }

    public List<T> getListagemGeral() {
        limpar();

        return getListagem();
    }

    protected void Refresh() {
        entidade = repositorio.Refresh(getEntidade());
    }

    public boolean isNew() {
        if (getEntidade() == null) {
            return true;
        } else if (getEntidade().getId() == null) {
            return true;
        } else {
            return getEntidade().getId() == 0;
        }
    }

    public boolean isEditing() {
        if (getEntidade() == null) {
            return false;
        } else if (getEntidade().getId() == null) {
            return false;
        } else {
            return getEntidade().getId() > 0;
        }
    }

    public GenericDataModel getDataModel() {
        return new GenericDataModel<>(getListagem(), repositorio);
    }

    public void onRowSelect(SelectEvent event) {
        try {
            T obj = (T) event.getObject();
            setId(obj.getId());
            FacesContext.getCurrentInstance().getExternalContext().redirect(abrir());
        } catch (IOException ex) {

        }
    }

    public Arquivo criaArquivo(UploadedFile upload) {
        try {
            Arquivo arquivo = arqDAO.Salvar(upload.getInputstream(), upload.getFileName(), getDIRETORIO_ARQUIVOS(), getUsuarioCorrente());

            return arquivo;
        } catch (IOException ex) {
            Logger.getLogger(ControllerBaseEntidade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }

    public Entidade getSessaoNaoNula(String key, Repositorio dao) {
        try {
            Entidade tmp = super.getSessao(key, dao);
            if (tmp == null) {
                return (Entidade) dao.getTipo().newInstance();
            } else {
                return tmp;
            }
        } catch (InstantiationException | IllegalAccessException ex) {
            return null;
        }

    }

    public String getPaginaEdicao() {
        return paginaEdicao;
    }

    public void setPaginaEdicao(String paginaEdicao) {
        this.paginaEdicao = paginaEdicao;
    }

    public String getPaginaListagem() {
        return paginaListagem;
    }

    public void setPaginaListagem(String paginaListagem) {
        this.paginaListagem = paginaListagem;
    }

}
