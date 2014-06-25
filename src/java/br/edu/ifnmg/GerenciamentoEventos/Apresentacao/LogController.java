/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Log;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.LogRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseEntidade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Recurso;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.RecursoTipo;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PessoaRepositorio;
import javax.inject.Named;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;

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
        /*id = 0L;
        setEntidade(new Log());
        setFiltro(new Log());
        */        
    }

    @EJB
    LogRepositorio dao;
    
    @EJB
    PessoaRepositorio pessoaDAO;

    @PostConstruct
    public void init() {
        setRepositorio(dao);
        setFiltro(new Log());
    }
    
    @Override
    public Log getFiltro() {
        if(getSessao("filtro_usuario") != null){
            filtro.setUsuario( pessoaDAO.Abrir( Long.parseLong(getSessao("filtro_usuario"))));
        }
        if(getSessao("filtro_data") != null){
            try {
                filtro.setDataEvento( DateFormat.getInstance().parse(getSessao("filtro_data"))) ;
            } catch (ParseException ex) {
                Logger.getLogger(LogController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return filtro;
    }

    @Override
    public void setFiltro(Log filtro) {
        this.filtro = filtro;
        if(filtro.getUsuario() != null){
            setSessao("filtro_usuario",filtro.getUsuario().getId().toString());
        }
        if(filtro.getDataEvento()!= null){
            setSessao("filtro_data", DateFormat.getInstance().format(filtro.getDataEvento()) );
        }
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
    public List<Log> getListagem() {
        return dao.Buscar(filtro);
    }

}
