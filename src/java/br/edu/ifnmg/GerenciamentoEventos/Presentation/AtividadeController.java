/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Presentation;



import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Alocacao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Atividade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Pessoa;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.AtividadeRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.EventoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Status;
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
        alocacao = new Alocacao();
        responsavel = new Pessoa();
        
    }
    
    Evento padrao;
    
    @EJB
    AtividadeRepositorio dao;
    
    @EJB
    EventoRepositorio evtDAO;
    
    Status[] status;
    
    Pessoa responsavel;
    
    Alocacao alocacao;
    
    @PostConstruct
    public void init() {
        setRepositorio(dao);
        checaEventoPadrao();
    }
    
    public void checaEventoPadrao(){
        String evt = getConfiguracao("EVENTO_PADRAO");
        if(evt != null){
            padrao = evtDAO.Abrir(Long.parseLong(evt));
            getEntidade().setEvento(padrao);
            getFiltro().setEvento(padrao);
        }
    }

    public List<Atividade> autoCompleteAtividade(String query) {
        Atividade i = new Atividade();
        i.setNome(query);
        return dao.Buscar(i);
    }

    @Override
    public void filtrar() {
        checaEventoPadrao();
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
        checaEventoPadrao();
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

    public Status[] getStatus() {
        if(status == null){
            status = Status.values();
        }
        return status;
    }

    public Pessoa getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Pessoa responsavel) {
        this.responsavel = responsavel;
    }

    public Alocacao getAlocacao() {
        return alocacao;
    }

    public void setAlocacao(Alocacao alocacao) {
        this.alocacao = alocacao;
    }
    
    public void addResponsavel() {
        entidade.add(responsavel);
        SalvarAgregado(responsavel);
        responsavel = new Pessoa();
    }
    
    public void removeResponsavel() {
        entidade.remove(responsavel);
        RemoverAgregado(responsavel);
        responsavel = new Pessoa();
    }
    
    public void addAlocacao() {
        Rastrear(alocacao);
        alocacao.setInicio(entidade.getInicio());
        alocacao.setTermino(entidade.getTermino());
        entidade.add(alocacao);
        SalvarAgregado(alocacao);
        alocacao= new Alocacao();
    }
    
    public void removeAlocacao() {
        entidade.remove(alocacao);
        RemoverAgregado(alocacao);
        alocacao= new Alocacao();
    }

    
}
