/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Presentation;



import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Inscricao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.InscricaoItem;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.InscricaoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.EventoRepositorio;
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
@Named(value = "inscricaoController")
@SessionScoped
public class InscricaoController
        extends ControllerBaseEntidade<Inscricao>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public InscricaoController() {
        id = 0L;
        setEntidade(new Inscricao());
        setFiltro(new Inscricao());
        item = new InscricaoItem();
    }
    
    Evento padrao;
    
    @EJB
    InscricaoRepositorio dao;
    
    @EJB
    EventoRepositorio evtDAO;
    
    InscricaoItem item;
    
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

    public List<Inscricao> autoCompleteInscricao(String query) {
        Inscricao i = new Inscricao();
        Long id = Long.parseLong(query);
        i.setId(id);
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
        return "listagemInscricoes.xtml";
    }

    @Override
    public String abrir() {
        setEntidade(dao.Abrir(id));
        return "editarInscricao.xhtml";
    }

    @Override
    public String cancelar() {
        return "listagemInscricoes.xhtml";
    }

    @Override
    public void limpar() {
        checaEventoPadrao();
        setEntidade(new Inscricao());
    }

    @Override
    public String novo() {
        limpar();
        return "editarInscricao.xhtml";
    }

    @Override
    public List<Inscricao> getListagem() {
        if (listagem == null) {
            filtrar();
        }
        return listagem;
    }

    public void setListagem(List<Inscricao> listagem) {
        this.listagem = listagem;
    } 
    
    public void addItem() {
        entidade = dao.Refresh(entidade);
        item.setEvento(entidade.getEvento());
        Rastrear(item);
        entidade.add(item);
        SalvarAgregado(item);
        item = new InscricaoItem();
    }
    
    public void removeItem() {
        entidade.remove(item);
        RemoverAgregado(item);
        item = new InscricaoItem();
    }
}
