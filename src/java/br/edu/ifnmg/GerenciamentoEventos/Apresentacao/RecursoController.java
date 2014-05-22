/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao;



import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Recurso;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.RecursoTipo;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.RecursoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseEntidade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.AlocacaoStatus;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author petronio
 */
@Named(value = "recursoController")
@SessionScoped
public class RecursoController
        extends ControllerBaseEntidade<Recurso>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public RecursoController() {
        id = 0L;
        setEntidade(new Recurso());
        setFiltro(new Recurso());
    }
    
    @EJB
    RecursoRepositorio dao;
    
    @PostConstruct
    public void init() {
        setRepositorio(dao);
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
        return "listagemRecursos.xtml";
    }

    @Override
    public String abrir() {
        setEntidade(dao.Abrir(id));
        return "editarRecurso.xhtml";
    }

    @Override
    public String cancelar() {
        return "listagemRecursos.xhtml";
    }

    @Override
    public void limpar() {
        
        setEntidade(new Recurso());
    }

    @Override
    public String novo() {
        limpar();
        return "editarRecurso.xhtml";
    }


    public RecursoTipo[] getTipos() {
        return RecursoTipo.values();
    }

    public AlocacaoStatus[] getStatus() {
        return AlocacaoStatus.values();
    }
    
}
