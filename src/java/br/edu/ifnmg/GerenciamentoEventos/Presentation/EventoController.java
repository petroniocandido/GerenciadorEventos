/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Presentation;



import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.EventoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Presentation.Comum.ControllerBaseEntidade;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import org.primefaces.event.FileUploadEvent;

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
    
    
        
    @PostConstruct
    public void init() {
        setRepositorio(dao);
    }

    public List<Evento> autoCompleteEvento(String query) {
        Evento i = new Evento();
        i.setNome(query);
        return dao.Buscar(i);
    }

    @Override
    public void filtrar() {
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

    @Override
    public List<Evento> getListagem() {
        if (listagem == null) {
            filtrar();
        }
        return listagem;
    }

    public void setListagem(List<Evento> listagem) {
        this.listagem = listagem;
    }
    
    public void configurarEventoGlobal() {
        setConfiguracao("EVENTO_PADRAO", entidade.getId().toString());
        Mensagem("Sucesso", "Configuração global alterada com êxito!");
    }
    
    public void configurarEventoUsuario() {
        setConfiguracao(getUsuarioCorrente(), "EVENTO_PADRAO", entidade.getId().toString());
        Mensagem("Sucesso", "Configuração do usuário alterada com êxito!");
    }
    
    public void logoFileUpload(FileUploadEvent event) {  
        entidade = dao.Refresh(entidade);
        entidade.setLogo(criaArquivo(event.getFile()));
        if(dao.Salvar(entidade)){
            Mensagem("Sucesso", "Arquivo anexado com êxito!");
            AppendLog("Anexou o arquivo " + entidade.getLogo() + " ao evento " + entidade);
        } else {
            Mensagem("Falha", "Falha ao anexar o arquivo!");
            AppendLog("Erro ao anexar o arquivo " + entidade.getLogo() + " ao evento " + entidade + ":" + dao.getErro());
        }
        
    }
    
    public void bannerFileUpload(FileUploadEvent event) {  
        entidade = dao.Refresh(entidade);
        entidade.setBanner(criaArquivo(event.getFile()));
        if(dao.Salvar(entidade)){
            Mensagem("Sucesso", "Arquivo anexado com êxito!");
            AppendLog("Anexou o arquivo " + entidade.getBanner()+ " ao evento " + entidade);
        } else {
            Mensagem("Falha", "Falha ao anexar o arquivo!");
            AppendLog("Erro ao anexar o arquivo " + entidade.getBanner()+ " ao evento " + entidade + ":" + dao.getErro());
        }
    }  
    
}
