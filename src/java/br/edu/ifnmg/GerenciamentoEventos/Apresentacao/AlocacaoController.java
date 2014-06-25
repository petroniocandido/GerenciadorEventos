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
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.PessoaRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.RecursoRepositorio;
import javax.inject.Named;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    }
    
    @Override
    public Alocacao getFiltro() {
        if(getSessao("filtro_responsavel") != null){
            filtro.setResponsavel(pessoaDAO.Abrir( Long.parseLong(getSessao("filtro_responsavel"))));
        }
        
        if(getSessao("filtro_recurso") != null){
            filtro.setRecurso(recursoDAO.Abrir( Long.parseLong(getSessao("filtro_recurso"))));
        }
        
        if(getSessao("filtro_data") != null){
            try {
                filtro.setInicio(DateFormat.getInstance().parse(getSessao("filtro_data"))) ;
            } catch (ParseException ex) {
                Logger.getLogger(LogController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return filtro;
    }

    @Override
    public void setFiltro(Alocacao filtro) {
        this.filtro = filtro;
        if(filtro.getResponsavel() != null){
            setSessao("filtro_responsavel",filtro.getResponsavel().getId().toString());
        }
        if(filtro.getRecurso() != null){
            setSessao("filtro_recurso",filtro.getRecurso().getId().toString());
        }
        if(filtro.getInicio()!= null){
            setSessao("filtro_data", DateFormat.getInstance().format(filtro.getInicio()) );
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
