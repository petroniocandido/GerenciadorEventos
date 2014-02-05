/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Presentation.Comum;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Configuracao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Entidade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Pessoa;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.ConfiguracaoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.Repositorio;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author petronio
 * @param <T>
 */
public abstract class ControllerBaseEntidade<T extends Entidade> extends ControllerBase {
    
    protected Long id;
    
    @EJB
    ConfiguracaoRepositorio confDAO;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public abstract void limpar();
    public abstract void filtrar();
    public abstract void salvar();
    public abstract String apagar();
    public abstract String abrir();
    public abstract String cancelar();
    public abstract String novo();
    public abstract List<T> getListagem();
    
    protected T entidade, filtro;
    protected List<T> listagem;
    protected Repositorio<T> repositorio;

    public T getEntidade() {
        return entidade;
    }

    public void setEntidade(T entidade) {
        this.entidade = entidade;
    }
    
    public void setRepositorio(Repositorio repo){
        this.repositorio = repo;
    }

    public T getFiltro() {
        return filtro;
    }

    public void setFiltro(T filtro) {
        this.filtro = filtro;
    }
    
    
    
    protected void SalvarEntidade(){
        Rastrear(entidade);

        // salva o objeto no BD
        if (repositorio.Salvar(entidade)) {

            setId(entidade.getId());

            Mensagem("Sucesso", "Registro salvo com sucesso!");
            AppendLog("Editou a entidade " + entidade.getClass().getSimpleName() + " " +  entidade.getId() + "(" + entidade.toString() + ")");
        } else {
            MensagemErro("Falha", "Registro não foi salvo! Consulte o Log ou o administrador do sistema!");
            AppendLog("Falha ao editar a entidade " + entidade.getClass().getSimpleName() + " " +  entidade.getId() + "(" + entidade.toString() + ")" + ": " + repositorio.getErro().getMessage());
        }
    }
    
    protected void ApagarEntidade(){
        Rastrear(entidade);

        // salva o objeto no BD
        if (repositorio.Apagar(entidade)) {

            Mensagem("Sucesso", "Registro removido com sucesso!");
            AppendLog("Apagou a entidade " + entidade.getClass().getSimpleName() + " " +  entidade.getId() + "(" + entidade.toString() + ")");
        } else {
            MensagemErro("Falha", "Registro não foi removido! Consulte o Log ou o administrador do sistema!");
            AppendLog("Falha ao remover a entidade " + entidade.getClass().getSimpleName() + " " +  entidade.getId() + "(" + entidade.toString() + ")" + ": " + repositorio.getErro().getMessage());
        }
    }
    
    protected  void SalvarAgregado(Entidade obj){
        Rastrear(entidade);
        if(repositorio.Salvar(entidade)) {
            Mensagem("Sucesso", "Item adicionado com sucesso!");
            AppendLog("Adicionando " + obj.getClass().getSimpleName() + " " + obj.getId() + "(" + obj.toString() + ") à entidade " + entidade.getClass().getSimpleName() + " " +  entidade.getId() + "(" + entidade.toString() + ")");
        }
        else {
            AppendLog("Falha ao adicionar " + obj.getClass().getSimpleName() + " " + obj.getId() + "(" + obj.toString() + ") à entidade " + entidade.getClass().getSimpleName() + " " +  entidade.getId() + "(" + entidade.toString() + ")" +
                    ": " + repositorio.getErro().getMessage());
            MensagemErro("Falha", "Item não adicionado! Consulte o log.");
        }
    }
    
    protected  void SalvarAgregado(Object obj){
        Rastrear(entidade);
        if(repositorio.Salvar(entidade)) {
            Mensagem("Sucesso", "Item adicionado com sucesso!");
            AppendLog("Adicionando " + obj.toString() + " à entidade " + entidade.getClass().getSimpleName() + " " +  entidade.getId() + "(" + entidade.toString() + ")");
        }
        else {
            AppendLog("Falha ao adicionar " + obj.toString() + " à entidade " + entidade.getClass().getSimpleName() + " " +  entidade.getId() + "(" + entidade.toString() + ")" +
                    ": " + repositorio.getErro().getMessage());
            MensagemErro("Falha", "Item não adicionado! Consulte o log.");
        }
    }
    
    protected  void RemoverAgregado(Entidade obj){
        Rastrear(entidade);
        if(repositorio.Salvar(entidade)) {
            Mensagem("Sucesso", "Item removido com sucesso!");
            AppendLog("Removendo " + obj.getClass().getSimpleName() + " " + obj.getId() + "(" + obj.toString() + ") à entidade " + entidade.getClass().getSimpleName() + " " +  entidade.getId() + "(" + entidade.toString() + ")");
        }
        else {
            AppendLog("Falha ao remover " + obj.getClass().getSimpleName() + " " + obj.getId() + "(" + obj.toString() + ") à entidade " + entidade.getClass().getSimpleName() + " " +  entidade.getId() + "(" + entidade.toString() + ")" +
                    ": " + repositorio.getErro().getMessage());
            MensagemErro("Falha", "Item não removido! Consulte o log.");
        }
    }
    
    protected  void RemoverAgregado(Object obj){
        Rastrear(entidade);
        if(repositorio.Salvar(entidade)) {
            Mensagem("Sucesso", "Item removido com sucesso!");
            AppendLog("Removendo " + obj.toString() + " à entidade " + entidade.getClass().getSimpleName() + " " +  entidade.getId() + "(" + entidade.toString() + ")");
        }
        else {
            AppendLog("Falha ao remover " + obj.toString() + " da entidade " + entidade.getClass().getSimpleName() + " " +  entidade.getId() + "(" + entidade.toString() + ")" +
                    ": " + repositorio.getErro().getMessage());
            MensagemErro("Falha", "Item não removido! Consulte o log.");
        }
    }
    
    public boolean isNew(){
        if(getEntidade() == null){ 
            return true;        
        }
        else if(getEntidade().getId() == null){
            return true;
        }
        else {
            return getEntidade().getId() == 0;
        }
    }
    
    public boolean isEditing(){
        if(getEntidade() == null){ 
            return false;        
        }
        else if(getEntidade().getId() == null){
            return false;
        }
        else {
            return getEntidade().getId() > 0;
        }
    }
    
    public GenericDataModel getDataModel(){
        return new GenericDataModel<>(getListagem(),repositorio);
    }
    
    public void onRowSelect(SelectEvent event) {          
        try {
            T obj = (T)event.getObject();
            setId(obj.getId());
            FacesContext.getCurrentInstance().getExternalContext().redirect(abrir());
        } catch (IOException ex) {
            
        }
    } 
    
    public void setConfiguracao(String chave, String valor){
        confDAO.Set(chave, valor);
        AppendLog("Alterando configuração global" + chave + " = " + valor);
    }
    
    public void setConfiguracao(Pessoa usr, String chave, String valor){
        confDAO.Set(usr, chave, valor);
        AppendLog("Alterando configuração de usuário " + chave + " = " + valor);
    }
    
    public String getConfiguracao(String chave){
        Configuracao c = confDAO.Abrir(chave);
        if(c == null ) {
            c = confDAO.Abrir(getUsuarioCorrente(), chave);
        }
        
        if(c != null)
            return c.getValor();
        else
            return null;
    }
    
}