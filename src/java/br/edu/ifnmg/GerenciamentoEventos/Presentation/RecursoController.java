/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Presentation;



import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Recurso;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.RecursoTipo;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.RecursoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Presentation.Comum.ControllerBaseEntidade;
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
    
    RecursoTipo[] tipos;
        
    @PostConstruct
    public void init() {
        setRepositorio(dao);
    }

    public List<Recurso> autoCompleteRecurso(String query) {
        Recurso i = new Recurso();
        i.setNome(query);
        return dao.Buscar(i);
    }
    
    public List<Recurso> autoCompleteImoveis(String query) {
        Recurso i = new Recurso();
        i.setNome(query);
        i.setTipo(RecursoTipo.Imovel);
        return dao.Buscar(i);
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

    @Override
    public List<Recurso> getListagem() {
        if (listagem == null) {
            filtrar();
        }
        return listagem;
    }

    public void setListagem(List<Recurso> listagem) {
        this.listagem = listagem;
    }

    public RecursoTipo[] getTipos() {
        if(tipos == null){
            tipos = RecursoTipo.values();
        }
        return tipos;
    }
    
    
}
