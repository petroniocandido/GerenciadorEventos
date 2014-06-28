/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao;



import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.AlocacaoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.EventoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseEntidade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Alocacao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.AlocacaoStatus;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Pessoa;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Recurso;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PessoaRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.RecursoRepositorio;
import javax.inject.Named;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author petronio
 */
@Named(value = "alocacaoController")
@RequestScoped
public class AlocacaoController
        extends ControllerBaseEntidade<Alocacao>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public AlocacaoController() {
    }
    
    Evento padrao;
    
    @EJB
    AlocacaoRepositorio dao;
    
    @EJB
    EventoRepositorio evtDAO;
    
    @EJB
    PessoaRepositorio pessoaDAO;
    
    @EJB
    RecursoRepositorio recursoDAO;
    
    @PostConstruct
    public void init() {
        setRepositorio(dao);        
        setFiltro(new Alocacao());
        checaEventoPadrao();
        setPaginaEdicao("editarAlocacao.xhtml");
        setPaginaListagem("listagemAlocacoes.xtml");
    }
    
    @Override
    public Alocacao getFiltro() {
        filtro.setResponsavel((Pessoa)getSessao("alcctrl_responsavel",pessoaDAO));
        filtro.setRecurso((Recurso)getSessao("alcctrl_recurso",recursoDAO));
        filtro.setInicio(getSessaoData("alcctrl_data")) ;
        return filtro;
    }

    @Override
    public void setFiltro(Alocacao filtro) {
        this.filtro = filtro;
        setSessao("alcctrl_responsavel",filtro.getResponsavel());
        setSessao("alcctrl_recurso",filtro.getRecurso());
        setSessao("alcctrl_data", filtro.getInicio());        
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
    }

    @Override
    public void limpar() {
        checaEventoPadrao();
        setEntidade(new Alocacao());
    }


    public AlocacaoStatus[] getStatus() {
        return AlocacaoStatus.values();
    }
    
    
    public void concluirItem(){
        getEntidade().setStatus(AlocacaoStatus.Concluido);
        if(dao.Salvar(entidade)){
            Mensagem("Confirmação", "Alocação concluída!");
        } else {
            AppendLog("Erro ao concluir alocação: " + dao.getErro().getMessage());
            MensagemErro("Atenção", "Erro ao concluir alocação! Consulte o administrador do sistema!");
        }
    }
    
    public void cancelarItem(){
        getEntidade().setStatus(AlocacaoStatus.Cancelado);
        if(dao.Salvar(entidade)){
            Mensagem("Confirmação", "Alocação cancelada!");
        } else {
            AppendLog("Erro ao cancelar alocação: " + dao.getErro().getMessage());
            MensagemErro("Atenção", "Erro ao cancelar alocação! Consulte o administrador do sistema!");
        }
    }    
}
