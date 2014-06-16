/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao;

import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.InscricaoItem;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.InscricaoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.EventoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseEntidade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Inscricao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.InscricaoCategoria;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.InscricaoStatus;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Pessoa;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.AtividadeRepositorio;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.ValueChangeEvent;

/**
 *
 * @author petronio
 */
@Named(value = "inscricaoItemController")
@SessionScoped
public class InscricaoItemController
        extends ControllerBaseEntidade<InscricaoItem>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public InscricaoItemController() {
        id = 0L;
        setEntidade(new InscricaoItem());
        setFiltro(new InscricaoItem());
    }

    Evento padrao;

    @EJB
    InscricaoRepositorio dao;

    @EJB
    EventoRepositorio evtDAO;

    @EJB
    AtividadeRepositorio atiDAO;

    @PostConstruct
    public void init() {
        setRepositorio(dao);
        checaEventoPadrao();
    }
    
    public List<InscricaoItem> getListagem(){
        return dao.Buscar(filtro);
    }


    public void checaEventoPadrao() {
        String evt = getConfiguracao("EVENTO_PADRAO");
        if (evt != null && padrao == null) {
            padrao = evtDAO.Abrir(Long.parseLong(evt));
            if (getEntidade().getEvento() == null) {
                getEntidade().setEvento(padrao);
            }
            if (getFiltro().getEvento() == null) {
                getFiltro().setEvento(padrao);
            }
        }
    }

    @Override
    public void filtrar() {
        checaEventoPadrao();

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
        return "listagemInscricoesAtividade.xtml";
    }

    @Override
    public String abrir() {
        setEntidade(dao.AbrirItem(id));
        return "editarInscricaoAtividade.xhtml";
    }

    @Override
    public String cancelar() {
        return "listagemInscricoesAtividade.xhtml";
    }

    @Override
    public void limpar() {
        checaEventoPadrao();
        setEntidade(new InscricaoItem());
    }

    @Override
    public String novo() {
        limpar();
        return "editarInscricaoAtividade.xhtml";
    }

    public InscricaoStatus[] getStatus() {
        return InscricaoStatus.values();
    }

    public InscricaoCategoria[] getCategorias() {
        return InscricaoCategoria.values();
    }

    public void checkIn(InscricaoItem itm) throws Exception {
        itm.setCompareceu(!itm.isCompareceu());
        if(dao.Salvar(itm)){
            Mensagem("Confirmação", "Presença registrada com êxito!");
        } else {
            AppendLog("Erro ao registrar presença: " + dao.getErro().getMessage());
            MensagemErro("Atenção", "Erro ao registrar presença! Consulte o administrador do sistema!");
        }
    }
    
     public List<Pessoa> getPessoa() {
        List<Pessoa> pessoas = new ArrayList<>();
        pessoas.add(entidade.getPessoa());
        return pessoas;
    }
    
    public List<Pessoa> getPessoas() {
        List<Pessoa> pessoas = new ArrayList<>();
        for(Inscricao i : getListagem())
            pessoas.add(i.getPessoa());
        
        return pessoas;
    }

}
