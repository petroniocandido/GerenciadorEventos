/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao;



import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Inscricao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.InscricaoItem;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Lancamento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.InscricaoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.EventoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseEntidade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.InscricaoCategoria;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.InscricaoStatus;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Pessoa;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.InscricaoService;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PessoaRepositorio;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author petronio
 */
@Named(value = "inscricaoController")
@RequestScoped
public class InscricaoController
        extends ControllerBaseEntidade<Inscricao>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public InscricaoController() {
        item = new InscricaoItem();
    }
    
    Evento padrao;
    
    @EJB
    InscricaoRepositorio dao;
    
    @EJB
    EventoRepositorio evtDAO;
    
    @EJB
    PessoaRepositorio pessoaDAO;
    
    @EJB
    InscricaoService service;
    
    InscricaoItem item;
    
    @PostConstruct
    public void init() {
        setRepositorio(dao);
        checaEventoPadrao();
        setPaginaEdicao("editarInscricao.xhtml");
        setPaginaListagem("listagemInscricoes.xtml");
    }
    
    @Override
    public Inscricao getFiltro() {
        if (filtro == null) {
            filtro = new Inscricao();
            filtro.setPessoa((Pessoa) getSessao("insctrl_pessoa", pessoaDAO));
            filtro.setEvento((Evento) getSessao("insctrl_evento", evtDAO));
            String tmp = getSessao("insctrl_id");
            filtro.setId(tmp != null ? Long.parseLong(tmp) : null);
            tmp = getSessao("insctrl_cat");
            filtro.setCategoria((tmp != null) ? InscricaoCategoria.valueOf(getSessao("insctrl_cat")) : null);
        }
        return filtro;
    }

    @Override
    public void setFiltro(Inscricao filtro) {
        this.filtro = filtro;
        if (filtro != null) {
            setSessao("insctrl_pessoa", filtro.getPessoa());
            setSessao("insctrl_evento", filtro.getEvento());
            setSessao("insctrl_id", filtro.getId() != null ? filtro.getId().toString() : null);
            setSessao("insctrl_cat", filtro.getCategoria()!= null ? filtro.getCategoria().name() : null);
        }
    }
    
    
    public void checaEventoPadrao() {
        String evt = getConfiguracao("EVENTO_PADRAO");
        if (evt != null && padrao == null) {
            padrao = evtDAO.Abrir(Long.parseLong(evt));
            if(getEntidade().getEvento() == null)
                getEntidade().setEvento(padrao);
            if(getFiltro().getEvento() == null)
                getFiltro().setEvento(padrao);
        }
    }

    @Override
    public void filtrar() {
        checaEventoPadrao();
        setFiltro(filtro);
    }

    @Override
    public void limpar() {
        checaEventoPadrao();
        setEntidade(new Inscricao());
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

    public InscricaoItem getItem() {
        return item;
    }

    public void setItem(InscricaoItem item) {
        this.item = item;
    }
       
    public void addItem() {
        Refresh();
        item.setEvento(entidade.getEvento());
        Rastrear(item);
        entidade.add(item);
        SalvarAgregado(item);
        item = new InscricaoItem();
    }
    
    public void removeItem() {
        Refresh();
        entidade.remove(item);
        RemoverAgregado(item);
        item = new InscricaoItem();
    }
    
    public String gerarLancamento() {
        Lancamento l = getEntidade().criarLancamento(getUsuarioCorrente());
        Rastrear(l);
        Rastrear(entidade);
        dao.Salvar(entidade);
        setSessao("Lancamentoentidade", l);
        return "editarLancamento.xhtml";
    }


    public InscricaoStatus[] getStatus() {
        return InscricaoStatus.values();
    }
    
    public InscricaoCategoria[] getCategorias() {
        return InscricaoCategoria.values();
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
