/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao;



import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Perfil;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Permissao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PerfilRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseEntidade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Log;
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
import javax.faces.event.ValueChangeEvent;

/**
 *
 * @author petronio
 */
@Named(value = "perfilController")
@RequestScoped
public class PerfilController
        extends ControllerBaseEntidade<Perfil>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public PerfilController() {
    }
    
    @EJB
    PerfilRepositorio dao;
    
    @EJB
    PessoaRepositorio daoP;
    
    Permissao permissao;
   
    
    @PostConstruct
    public void init() {
        setRepositorio(dao);
        setFiltro(new Perfil());
    }
    
     @Override
    public Perfil getFiltro() {
        if(getSessao("filtro_nome") != null){
            filtro.setNome(getSessao("filtro_nome"));
        }
        return filtro;
    }

    @Override
    public void setFiltro(Perfil filtro) {
        this.filtro = filtro;
        if(filtro.getNome()!= null){
            setSessao("filtro_nome",filtro.getNome());
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
    
    public List<Pessoa> getPessoas() {
        return daoP.IgualA("perfil", entidade).Buscar();
    }
    
}
