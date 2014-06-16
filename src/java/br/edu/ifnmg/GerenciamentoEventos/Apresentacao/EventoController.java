/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao;



import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.EventoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseEntidade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Pessoa;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Status;
import javax.inject.Named;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author petronio
 */
@Named(value = "eventoController")
@SessionScoped
public class EventoController
        extends ControllerBaseEntidade<Evento>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public EventoController() {
        id = 0L;
        setEntidade(new Evento());
        setFiltro(new Evento());
    }
    
    @EJB
    EventoRepositorio dao;
    
    UploadedFile arquivo;

    public UploadedFile getArquivo() {
        return arquivo;
    }

    public void setArquivo(UploadedFile arquivo) {
        this.arquivo = arquivo;
    }
        
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
        return "listagemEventos.xtml";
    }

    @Override
    public String abrir() {
        setEntidade(dao.Abrir(id));
        return "editarEvento.xhtml";
    }

    @Override
    public String cancelar() {
        return "listagemEventos.xhtml";
    }

    @Override
    public void limpar() {
        
        setEntidade(new Evento());
    }

    @Override
    public String novo() {
        limpar();
        return "editarEvento.xhtml";
    }

    
    public void configurarEventoGlobal() {
        setConfiguracao("EVENTO_PADRAO", entidade.getId().toString());
        Mensagem("Sucesso", "Configuração global alterada com êxito!");
    }
    
    public void configurarEventoUsuario() {
        setConfiguracao(getUsuarioCorrente(), "EVENTO_PADRAO", entidade.getId().toString());
        Mensagem("Sucesso", "Configuração do usuário alterada com êxito!");
    }
    
    public void logoFileUpload() {  
        entidade = dao.Refresh(entidade);
        entidade.setLogo(criaArquivo(arquivo));
        if(dao.Salvar(entidade)){
            Mensagem("Sucesso", "Arquivo anexado com êxito!");
            AppendLog("Anexou o arquivo " + entidade.getLogo() + " ao evento " + entidade);
        } else {
            Mensagem("Falha", "Falha ao anexar o arquivo!");
            AppendLog("Erro ao anexar o arquivo " + entidade.getLogo() + " ao evento " + entidade + ":" + dao.getErro());
        }
        
    }
    
    public void bannerFileUpload() {  
        entidade = dao.Refresh(entidade);
        entidade.setBanner(criaArquivo(arquivo));
        if(dao.Salvar(entidade)){
            Mensagem("Sucesso", "Arquivo anexado com êxito!");
            AppendLog("Anexou o arquivo " + entidade.getBanner()+ " ao evento " + entidade);
        } else {
            Mensagem("Falha", "Falha ao anexar o arquivo!");
            AppendLog("Erro ao anexar o arquivo " + entidade.getBanner()+ " ao evento " + entidade + ":" + dao.getErro());
        }
    }  
    
     public Status[] getStatus() {
        return Status.values();
    }
     
     Pessoa responsavel;
     
     public void addResponsavel() {
        entidade = dao.Refresh(entidade);
        entidade.add(responsavel);
        SalvarAgregado(responsavel);
        responsavel = new Pessoa();
    }

    public void removeResponsavel() {
        entidade = dao.Refresh(entidade);
        entidade.remove(responsavel);
        RemoverAgregado(responsavel);
        responsavel = new Pessoa();
    }

    public Pessoa getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Pessoa responsavel) {
        this.responsavel = responsavel;
    }
    
    
    
}
