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
import javax.inject.Named;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author petronio
 */
@Named(value = "alocacaoController")
@SessionScoped
public class AlocacaoController
        extends ControllerBaseEntidade<Alocacao>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public AlocacaoController() {
        id = 0L;
        setEntidade(new Alocacao());
        setFiltro(new Alocacao());
    }
    
    Evento padrao;
    
    @EJB
    AlocacaoRepositorio dao;
    
    @EJB
    EventoRepositorio evtDAO;
    
    @PostConstruct
    public void init() {
        setRepositorio(dao);
        checaEventoPadrao();
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
    public void salvar() {
        
        SalvarEntidade();
        
        // atualiza a listagem
        filtrar();
    }

    @Override
    public String apagar() {
        ApagarEntidade();
        filtrar();
        return "listagemAlocacoes.xtml";
    }

    @Override
    public String abrir() {
        setEntidade(dao.Abrir(id));
        return "editarAlocacao.xhtml";
    }

    @Override
    public String cancelar() {
        return "listagemAlocacoes.xhtml";
    }

    @Override
    public void limpar() {
        checaEventoPadrao();
        setEntidade(new Alocacao());
    }

    @Override
    public String novo() {
        limpar();
        return "editarAlocacao.xhtml";
    }
  

    public AlocacaoStatus[] getStatus() {
        return AlocacaoStatus.values();
    }
    
    
    public void concluir(Alocacao tmp){
        tmp.setStatus(AlocacaoStatus.Concluido);
        if(dao.Salvar(tmp)){
            Mensagem("Confirmação", "Alocação concluída!");
        } else {
            AppendLog("Erro ao concluir alocação: " + dao.getErro().getMessage());
            MensagemErro("Atenção", "Erro ao concluir alocação! Consulte o administrador do sistema!");
        }
    }
    
    public void cancelar(Alocacao tmp){
        tmp.setStatus(AlocacaoStatus.Cancelado);
        if(dao.Salvar(tmp)){
            Mensagem("Confirmação", "Alocação cancelada!");
        } else {
            AppendLog("Erro ao cancelar alocação: " + dao.getErro().getMessage());
            MensagemErro("Atenção", "Erro ao cancelar alocação! Consulte o administrador do sistema!");
        }
    }
    
}
