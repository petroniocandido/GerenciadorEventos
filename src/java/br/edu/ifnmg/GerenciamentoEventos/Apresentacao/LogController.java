/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Log;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.LogRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseEntidade;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author petronio
 */
@Named(value = "logController")
@RequestScoped
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
        return dao.Buscar(filtro);
    }

}
