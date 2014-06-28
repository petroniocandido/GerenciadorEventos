/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Log;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.LogRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseEntidade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Pessoa;
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
        setPaginaEdicao("editarLog.xhtml");
        setPaginaListagem("listarLogs.xtml");
    }

    @Override
    public Log getFiltro() {
        filtro.setUsuario((Pessoa) getSessao("logctrl_usuario", pessoaDAO));
        filtro.setDataEvento(getSessaoData("logctrl_data"));
        return filtro;
    }

    @Override
    public void setFiltro(Log filtro) {
        this.filtro = filtro;
        setSessao("logctrl_usuario", filtro.getUsuario());
        setSessao("logctrl_data", filtro.getDataEvento());
    }

    @Override
    public void limpar() {

        setEntidade(new Log());
    }

}
