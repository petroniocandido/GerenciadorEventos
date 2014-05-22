/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao;



import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Perfil;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Permissao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PerfilRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseEntidade;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ValueChangeEvent;

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
    
    Permissao permissao;
   
    
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

       
    public void valueChangeListener(ValueChangeEvent evt){
        entidade = dao.Refresh(entidade);
        if((boolean)evt.getNewValue())
            entidade.add(permissao);
        else
            entidade.remove(permissao);
    }

    public Permissao getPermissao() {
        return permissao;
    }

    public void setPermissao(Permissao permissao) {
        this.permissao = permissao;
    }
    
    
}
