/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Presentation;



import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Atividade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.AtividadeRepositorio;
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
@Named(value = "atividadeController")
@SessionScoped
public class AtividadeController
        extends ControllerBaseEntidade<Atividade>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public AtividadeController() {
        id = 0L;
        setEntidade(new Atividade());
        setFiltro(new Atividade());
    }
    
    @EJB
    AtividadeRepositorio dao;
        
    @PostConstruct
    public void init() {
        setRepositorio(dao);
    }

    public List<Atividade> autoCompleteAtividade(String query) {
        Atividade i = new Atividade();
        i.setNome(query);
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
        return "listagemAtividades.xtml";
    }

    @Override
    public String abrir() {
        setEntidade(dao.Abrir(id));
        return "editarAtividade.xhtml";
    }

    @Override
    public String cancelar() {
        return "listagemAtividades.xhtml";
    }

    @Override
    public void limpar() {
        
        setEntidade(new Atividade());
    }

    @Override
    public String novo() {
        limpar();
        return "editarAtividade.xhtml";
    }

    @Override
    public List<Atividade> getListagem() {
        if (listagem == null) {
            filtrar();
        }
        return listagem;
    }

    public void setListagem(List<Atividade> listagem) {
        this.listagem = listagem;
    }
    
}
