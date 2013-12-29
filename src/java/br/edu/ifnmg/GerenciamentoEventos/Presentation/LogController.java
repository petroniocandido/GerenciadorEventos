/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Presentation;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Log;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.LogRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Presentation.Comum.ControllerBaseEntidade;
import java.io.IOException;
import javax.inject.Named;
import java.io.Serializable;
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
@Named(value = "logController")
@SessionScoped
public class LogController
        extends ControllerBaseEntidade<Log>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public LogController() {
        id = 0L;
        setEntidade(new Log());
        setFiltro(new Log());
    }

    @EJB
    LogRepositorio dao;

    @PostConstruct
    public void init() {
        setRepositorio(dao);
    }

    @Override
    public void filtrar() {
        listagem = dao.Buscar(filtro);
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
        return "listarLogs.xtml";
    }

    @Override
    public String abrir() {
        setEntidade(dao.Abrir(id));
        return "editarLog.xhtml";
    }

    @Override
    public String cancelar() {
        return "listagemLogs.xhtml";
    }

    @Override
    public void limpar() {

        setEntidade(new Log());
    }

    @Override
    public String novo() {
        limpar();
        return "editarLog.xhtml";
    }

    
    @Override
    public void onRowSelect(SelectEvent event) {
        Log obj = (Log) event.getObject();
        setEntidade(obj);
    }

    @Override
    public List<Log> getListagem() {
        if (listagem == null) {
            filtrar();
        }
        return listagem;
    }

    public void setListagem(List<Log> listagem) {
        this.listagem = listagem;
    }
}
