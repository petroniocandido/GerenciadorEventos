/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Presentation;



import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Atividade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.AtividadeTipo;
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
@Named(value = "atividadeTipoController")
@SessionScoped
public class AtividadeTipoController
        extends ControllerBaseEntidade<AtividadeTipo>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public AtividadeTipoController() {
        id = 0L;
        setEntidade(new AtividadeTipo());
        setFiltro(new AtividadeTipo());
    }
    
    @EJB
    AtividadeRepositorio dao;
    
    @PostConstruct
    public void init() {
        setRepositorio(dao);
    }

    public List<AtividadeTipo> autoCompleteAtividadeTipo(String query) {
        AtividadeTipo i = new AtividadeTipo();
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
        return "listagemAtividadeTipos.xtml";
    }

    @Override
    public String abrir() {
        setEntidade(dao.AbrirTipo(id));
        return "editarAtividadeTipo.xhtml";
    }

    @Override
    public String cancelar() {
        return "listagemAtividadeTipos.xhtml";
    }

    @Override
    public void limpar() {
        
        setEntidade(new AtividadeTipo());
    }

    @Override
    public String novo() {
        limpar();
        return "editarAtividadeTipo.xhtml";
    }

    @Override
    public List<AtividadeTipo> getListagem() {
        if (listagem == null) {
            filtrar();
        }
        return listagem;
    }

    public void setListagem(List<AtividadeTipo> listagem) {
        this.listagem = listagem;
    }

    
}
