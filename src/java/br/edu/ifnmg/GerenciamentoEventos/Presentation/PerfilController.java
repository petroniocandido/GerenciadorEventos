/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Presentation;



import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Perfil;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Perfil;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PerfilRepositorio;
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
@Named(value = "perfilController")
@SessionScoped
public class PerfilController
        extends ControllerBaseEntidade<Perfil>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public PerfilController() {
        id = 0L;
        setEntidade(new Perfil());
        setFiltro(new Perfil());
    }
    
    @EJB
    PerfilRepositorio dao;
   
    
    @PostConstruct
    public void init() {
        setRepositorio(dao);
    }

    public List<Perfil> autoCompletePerfil(String query) {
        Perfil i = new Perfil();
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
        return "listarPerfis.xtml";
    }

    @Override
    public String abrir() {
        setEntidade(dao.Abrir(id));
        return "editarPerfil.xhtml";
    }

    @Override
    public String cancelar() {
        return "listagemPerfis.xhtml";
    }

    @Override
    public void limpar() {
        
        setEntidade(new Perfil());
    }

    @Override
    public String novo() {
        limpar();
        return "editarPerfil.xhtml";
    }

   

    @Override
    public List<Perfil> getListagem() {
        if (listagem == null) {
            filtrar();
        }
        return listagem;
    }

    public void setListagem(List<Perfil> listagem) {
        this.listagem = listagem;
    }
}
