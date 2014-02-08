/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Presentation;



import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Arquivo;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.ArquivoRepositorio;
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
@Named(value = "arquivoController")
@SessionScoped
public class ArquivoController
        extends ControllerBaseEntidade<Arquivo>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public ArquivoController() {
        id = 0L;
        setEntidade(new Arquivo());
        setFiltro(new Arquivo());
        
    }
    
    @EJB
    ArquivoRepositorio dao;
    
    
    @PostConstruct
    public void init() {
        setRepositorio(dao);        
    }

    public List<Arquivo> autoCompleteArquivo(String query) {
        Arquivo i = new Arquivo();
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
        return "listagemArquivos.xtml";
    }

    @Override
    public String abrir() {
        setEntidade(dao.Abrir(id));
        return "editarArquivo.xhtml";
    }

    @Override
    public String cancelar() {
        return "listagemArquivos.xhtml";
    }

    @Override
    public void limpar() {
        setEntidade(new Arquivo());
    }

    @Override
    public String novo() {
        limpar();
        return "editarArquivo.xhtml";
    }

    @Override
    public List<Arquivo> getListagem() {
        if (listagem == null) {
            filtrar();
        }
        return listagem;
    }

    public void setListagem(List<Arquivo> listagem) {
        this.listagem = listagem;
    }

    
}