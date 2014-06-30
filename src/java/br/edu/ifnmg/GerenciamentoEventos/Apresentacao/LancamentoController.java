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
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Pessoa;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.InscricaoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PessoaRepositorio;
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
@Named(value = "lancamentoController")
@RequestScoped
public class LancamentoController
        extends ControllerBaseEntidade<Lancamento>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public LancamentoController() {
    }
    
    @EJB
    LancamentoRepositorio dao;
    
    @EJB
    InscricaoRepositorio daoInsc;
    
    @EJB
    PessoaRepositorio daoPessoa;
    
    Inscricao inscricao;
    
    @PostConstruct
    public void init() {
        setRepositorio(dao);        
        setPaginaEdicao("editarLancamento.xhtml");
        setPaginaListagem("listagemLancamentos.xtml");
    }
    
     @Override
    public Lancamento getFiltro() {
        if (filtro == null) {
            filtro = new Lancamento();
            filtro.setCliente((Pessoa)getSessao("lcctrl_cliente", daoPessoa));
            filtro.setBaixa(getSessaoData("lcctrl_baixa"));
            String tmp = getSessao("lcctrl_tipo");
            filtro.setStatus((tmp != null) ? LancamentoStatus.valueOf(getSessao("lcctrl_tipo")) : null);
        }
        return filtro;
    }

    @Override
    public void setFiltro(Lancamento filtro) {
        this.filtro = filtro;
        setSessao("lcctrl_cliente", filtro.getCliente());
        setSessao("lcctrl_baixa", filtro.getBaixa());
        setSessao("lcctrl_tipo", filtro.getStatus()!= null ? filtro.getStatus().name() : null);
    }

    @Override
    public void limpar() {
        setEntidade(new Lancamento());
    }

    public LancamentoStatus[] getStatus() {
        return LancamentoStatus.values();
    }

    public Inscricao getInscricao() {
        if(inscricao == null){
            inscricao = (Inscricao)getSessaoNaoNula("lcctrl_insc", daoInsc);
        }
        return inscricao;
    }

    public void setInscricao(Inscricao inscricao) {
        this.inscricao = inscricao;
        setSessao("lcctrl_insc", inscricao);
    }
    
    public void addInscricao() {
        entidade = dao.Refresh(getEntidade());
        entidade.add(getInscricao());
        dao.Salvar(entidade);
        setInscricao(new Inscricao());
    }

    public void removeInscricao() {
        entidade = dao.Refresh(getEntidade());
        entidade.remove(getInscricao());
        dao.Salvar(entidade);
        setInscricao(new Inscricao());
    }
    
    public void baixarLancamento() {
        entidade = dao.Refresh(getEntidade());
        entidade.baixar(getUsuarioCorrente());
        if(dao.Salvar(entidade)) {
            Mensagem("Sucesso", "Lançamento baixado com sucesso!");
        } else {
            MensagemErro("Falha", "Lançamento não foi baixado! Consulte o administrador");
        }
    }
    
    public void cancelarLancamento() {
        entidade = dao.Refresh(getEntidade());
        entidade.cancelar(getUsuarioCorrente());
        if(dao.Salvar(entidade)) {
            Mensagem("Sucesso", "Lançamento cancelado com sucesso!");
        } else {
            MensagemErro("Falha", "Lançamento não foi cancelado! Consulte o administrador");
        }
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
