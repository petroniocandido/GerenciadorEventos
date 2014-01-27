/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Presentation;



import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Permissao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Permissao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PermissaoRepositorio;
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
@Named(value = "permissaoController")
@SessionScoped
public class PermissaoController
        extends ControllerBaseEntidade<Permissao>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public PermissaoController() {
        id = 0L;
        setEntidade(new Permissao());
        setFiltro(new Permissao());
    }
    
    @EJB
    PermissaoRepositorio dao;
   
    
    @PostConstruct
    public void init() {
        setRepositorio(dao);
    }

    public List<Permissao> autoCompletePermissao(String query) {
        Permissao i = new Permissao();
        i.setUri(query);
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
        return "listarPermissoes.xtml";
    }

    @Override
    public String abrir() {
        setEntidade(dao.Abrir(id));
        return "editarPermissao.xhtml";
    }

    @Override
    public String cancelar() {
        return "listagemPermissoes.xhtml";
    }

    @Override
    public void limpar() {
        
        setEntidade(new Permissao());
    }

    @Override
    public String novo() {
        limpar();
        return "editarPermissao.xhtml";
    }

   

    @Override
    public List<Permissao> getListagem() {
        if (listagem == null) {
            filtrar();
        }
        return listagem;
    }
    
    List<Permissao> todos;
    public List<Permissao> getListagemGeral() {
        if (todos == null) {
            todos = dao.Buscar(new Permissao());
        }
        return todos;
    }

    public void setListagem(List<Permissao> listagem) {
        this.listagem = listagem;
    }
}
