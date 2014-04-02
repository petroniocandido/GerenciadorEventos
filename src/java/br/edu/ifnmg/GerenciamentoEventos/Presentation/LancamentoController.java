/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Presentation;



import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Lancamento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.LancamentoStatus;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.LancamentoRepositorio;
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
@Named(value = "lancamentoController")
@SessionScoped
public class LancamentoController
        extends ControllerBaseEntidade<Lancamento>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public LancamentoController() {
        id = 0L;
        setEntidade(new Lancamento());
        setFiltro(new Lancamento());
        
    }
    
    @EJB
    LancamentoRepositorio dao;
    
    LancamentoStatus status[];
    
    @PostConstruct
    public void init() {
        setRepositorio(dao);        
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
        return "listagemLancamentos.xtml";
    }

    @Override
    public String abrir() {
        setEntidade(dao.Abrir(id));
        return "editarLancamento.xhtml";
    }

    @Override
    public String cancelar() {
        return "listagemLancamentos.xhtml";
    }

    @Override
    public void limpar() {
        setEntidade(new Lancamento());
    }

    @Override
    public String novo() {
        limpar();
        return "editarLancamento.xhtml";
    }

    @Override
    public List<Lancamento> getListagem() {
        if (listagem == null) {
            filtrar();
        }
        return listagem;
    }

    public void setListagem(List<Lancamento> listagem) {
        this.listagem = listagem;
    }

    public LancamentoStatus[] getStatus() {
        if(status == null){
            status = LancamentoStatus.values();
        }
        return status;
    }

    
}
