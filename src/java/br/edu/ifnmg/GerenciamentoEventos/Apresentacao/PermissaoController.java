/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao;



import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Permissao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PermissaoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseEntidade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Perfil;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author petronio
 */
@Named(value = "permissaoController")
@RequestScoped
public class PermissaoController
        extends ControllerBaseEntidade<Permissao>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public PermissaoController() {
    }
    
    @EJB
    PermissaoRepositorio dao;
   
    
    @PostConstruct
    public void init() {
        setRepositorio(dao);        
        setFiltro(new Permissao());
    }
    
      @Override
    public Permissao getFiltro() {
        if(getSessao("filtro_uri") != null){
            filtro.setUri(getSessao("filtro_uri"));
        }
        return filtro;
    }

    @Override
    public void setFiltro(Permissao filtro) {
        this.filtro = filtro;
        if(filtro.getUri()!= null){
            setSessao("filtro_uri",filtro.getUri());
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
    public List<Permissao> getListagemGeral() {
        return dao.Buscar(new Permissao());
    }

}
