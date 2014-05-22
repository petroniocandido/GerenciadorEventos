/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao;



import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Inscricao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Lancamento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.LancamentoCategoria;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.LancamentoStatus;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.LancamentoTipo;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.LancamentoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseEntidade;
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
        inscricao = new Inscricao();
        
    }
    
    @EJB
    LancamentoRepositorio dao;
    
    Inscricao inscricao;
    
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


    public LancamentoStatus[] getStatus() {
        return LancamentoStatus.values();
    }

    public Inscricao getInscricao() {
        return inscricao;
    }

    public void setInscricao(Inscricao inscricao) {
        this.inscricao = inscricao;
    }
    
    public void addInscricao() {
        entidade = dao.Refresh(entidade);
        entidade.add(inscricao);
        dao.Salvar(entidade);
        inscricao = new Inscricao();
    }

    public void removeInscricao() {
        entidade = dao.Refresh(entidade);
        entidade.remove(inscricao);
        dao.Salvar(entidade);
        inscricao = new Inscricao();
    }
    
    public void baixarLancamento() {
        entidade = dao.Refresh(entidade);
        entidade.baixar(getUsuarioCorrente());
        dao.Salvar(entidade);
    }
    
    public void cancelarLancamento() {
        entidade = dao.Refresh(entidade);
        entidade.cancelar(getUsuarioCorrente());
        dao.Salvar(entidade);
    }
    
    public boolean isEditavel() {
        return entidade.editavel();
    }
    
    public String getCorStatus(LancamentoStatus s){
        if(s == null)
            return "white";
        
        switch(s){
            case Aberto:
                return "white";
            case Baixado:
                return "green";
            case Cancelado:
                return "gray";
            default:
                return "white";
        }
    }
    
    public List<LancamentoCategoria> getCategorias() {
        return dao.BuscarCategorias(null);
    }

    public LancamentoTipo[] getTipos() {
        return LancamentoTipo.values();
    }
    
    
    
}
