/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Aplicacao;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Arquivo;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Entidade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.ArquivoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.Repositorio;
import java.io.IOException;
import java.lang.reflect.Field;
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
    
    @EJB
    ArquivoRepositorio arqDAO;

    public Long getId() {
        if(id == null || id == 0L){
            String tmp = getSessao("entidade");
            id = Long.parseLong(tmp);
        }
        return id;
    }

    public void setId(Long id) {
        if(id != null || id != 0L){
            setSessao("entidade", id.toString());
        }
        this.id = id;
    }

    public abstract void limpar();

    public void filtrar() {
    }

    public abstract void salvar();

    public abstract String apagar();

    public abstract String abrir();

    public abstract String cancelar();

    public abstract String novo();

    public List<T> getListagem(){
        return repositorio.Buscar(filtro);
    }

    protected T entidade, filtro;
    protected Repositorio<T> repositorio;

    public T getEntidade() {
        if (entidade == null) {
            entidade = (T) getSessao("entidade", repositorio);
        }
        return entidade;
    }

    public void setEntidade(T entidade) {
        this.entidade = entidade;
        setSessao("entidade", entidade);
    }

    public void setRepositorio(Repositorio repo) {
        this.repositorio = repo;
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
            Arquivo arquivo = arqDAO.Salvar(upload.getInputstream(), upload.getFileName(), getConfiguracao("DIRETORIO_ARQUIVOS"), getUsuarioCorrente());
            
            return arquivo;
        } catch (IOException ex) {
            Logger.getLogger(ControllerBaseEntidade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }
    
    @Override
    public Entidade getSessao(String key, Repositorio dao) {
        try {
            Entidade tmp = super.getSessao(key, dao);
            if (tmp == null)
                return (Entidade)dao.getTipo().newInstance();
            else
                return tmp;
        } catch (Exception ex) {
            return null;
        } 
        
                
    }

}
