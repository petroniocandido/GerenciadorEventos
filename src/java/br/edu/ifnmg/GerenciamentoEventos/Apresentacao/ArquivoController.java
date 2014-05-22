/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao;



import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Arquivo;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.ArquivoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseEntidade;
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
@Named(value = "arquivoController")
@SessionScoped
public class ArquivoController
        extends ControllerBaseEntidade<Arquivo>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public ArquivoController() {
        id = 0L;
        setEntidade(new Arquivo());
        setFiltro(new Arquivo());
        
    }
    
    @EJB
    ArquivoRepositorio dao;
    
    
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
        if (dao.Apagar(entidade, getConfiguracao("DIRETORIO_ARQUIVOS"))) {

            Mensagem("Sucesso", "Registro removido com sucesso!");
            AppendLog("Apagou a entidade " + entidade.getClass().getSimpleName() + " " + entidade.getId() + "(" + entidade.toString() + ")");
        } else {
            MensagemErro("Falha", "Registro não foi removido! Consulte o Log ou o administrador do sistema!");
            AppendLog("Falha ao remover a entidade " + entidade.getClass().getSimpleName() + " " + entidade.getId() + "(" + entidade.toString() + ")" + ": " + repositorio.getErro().getMessage());
        }
        filtrar();
        return "listagemArquivos.xtml";
    }

    @Override
    public String abrir() {
        setEntidade(dao.Abrir(id));
        return "editarArquivo.xhtml";
    }

    @Override
    public String cancelar() {
        return "listagemArquivos.xhtml";
    }

    @Override
    public void limpar() {
        setEntidade(new Arquivo());
    }

    @Override
    public String novo() {
        limpar();
        return "editarArquivo.xhtml";
    }

    
    
    public void arquivoFileUpload(FileUploadEvent event) {  
        entidade = criaArquivo(event.getFile());
        if(dao.Salvar(entidade)){
            Mensagem("Sucesso", "Arquivo anexado com êxito!");
            
        } else {
            Mensagem("Falha", "Falha ao anexar o arquivo!");
            
        }
        
    }

    
}
