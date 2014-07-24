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
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Atividade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Inscricao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.InscricaoCategoria;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.InscricaoStatus;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Pessoa;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.AtividadeRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.InscricaoService;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PessoaRepositorio;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;

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
    }

    Evento padrao;

    @EJB
    InscricaoRepositorio dao;

    @EJB
    EventoRepositorio evtDAO;

    @EJB
    AtividadeRepositorio atiDAO;
    
    @EJB
    PessoaRepositorio pessoaDAO;
    
    @EJB
    InscricaoService service;

    @PostConstruct
    public void init() {
        setRepositorio(dao);
        checaEventoPadrao();
        setPaginaEdicao("editarInscricaoAtividade.xhtml");
        setPaginaListagem("listagemInscricoesAtividade.xtml");
    }
    
    @Override
    public InscricaoItem getFiltro() {
        if (filtro == null) {
            filtro = new InscricaoItem();
            filtro.setPessoa((Pessoa) getSessao("iictrl_pessoa", pessoaDAO));
            filtro.setAtividade((Atividade) getSessao("iictrl_ativ", atiDAO));
            filtro.setEvento((Evento) getSessao("iictrl_evento", evtDAO));
            String tmp = getSessao("iictrl_cat");
            filtro.setCategoria((tmp != null) ? InscricaoCategoria.valueOf(getSessao("iictrl_cat")) : null);
        }
        return filtro;
    }

    @Override
    public void setFiltro(InscricaoItem filtro) {
        this.filtro = filtro;
        if (filtro != null) {
            setSessao("iictrl_pessoa", filtro.getPessoa());
            setSessao("iictrl_evento", filtro.getEvento());
            setSessao("iictrl_ativ", filtro.getAtividade());
            setSessao("iictrl_cat", filtro.getCategoria()!= null ? filtro.getCategoria().name() : null);
        }
    }
    
    @Override
    public List<InscricaoItem> getListagem(){
        return dao.Buscar(filtro);
    }
    
    @Override
    public InscricaoItem getEntidade() {
        if (entidade == null) {
            String tmp = getSessao("InscricaoItementidade");
            entidade = tmp != null ? dao.AbrirItem(Long.parseLong(tmp)) : new InscricaoItem();
        }
        return entidade;
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
    public String apagar() {
       Rastrear(getEntidade());

        // salva o objeto no BD
        if (service.cancelar(entidade)) {
            Mensagem("Sucesso", "Registro removido com sucesso!");
            AppendLog("Apagou a entidade " + entidade.getClass().getSimpleName() + " " + entidade.getId() + "(" + entidade.toString() + ")");
        } else {
            MensagemErro("Falha", "Registro não foi removido! Consulte o Log ou o administrador do sistema!");
            AppendLog("Falha ao remover a entidade " + entidade.getClass().getSimpleName() + " " + entidade.getId() + "(" + entidade.toString() + ")" + ": " + repositorio.getErro().getMessage());
        }
        filtrar();
        return getPaginaListagem();
    }

    @Override
    public void filtrar() {
        checaEventoPadrao();
    }

    @Override
    public void limpar() {
        checaEventoPadrao();
        setEntidade(new InscricaoItem());
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
        pessoas.add(getEntidade().getPessoa());
        return pessoas;
    }
    
    public List<Pessoa> getPessoas() {
        List<Pessoa> pessoas = new ArrayList<>();
        for(Inscricao i : getListagem())
            pessoas.add(i.getPessoa());
        
        return pessoas;
    }

}
