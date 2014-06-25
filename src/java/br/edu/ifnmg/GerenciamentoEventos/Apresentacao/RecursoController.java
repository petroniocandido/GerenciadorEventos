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
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author petronio
 */
@Named(value = "recursoController")
@RequestScoped
public class RecursoController
        extends ControllerBaseEntidade<Recurso>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public RecursoController() {
        /**id = 0L;
        setEntidade(new Recurso());        
        */ 
    }
    
    @EJB
    RecursoRepositorio dao;
    
    @PostConstruct
    public void init() {
        setRepositorio(dao);
        setFiltro(new Recurso());
    }
    
    @Override
    public Recurso getFiltro() {
        if(getSessao("filtro_nome") != null){
            filtro.setNome(getSessao("filtro_nome"));
        }
        if(getSessao("filtro_tipo") != null){
            filtro.setTipo( RecursoTipo.valueOf( getSessao("filtro_tipo") ));
        }
        return filtro;
    }

    @Override
    public void setFiltro(Recurso filtro) {
        this.filtro = filtro;
        if(filtro.getNome() != null){
            setSessao("filtro_nome",filtro.getNome());
        }
        if(filtro.getTipo() != null){
            setSessao("filtro_tipo",filtro.getTipo().name() );
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
