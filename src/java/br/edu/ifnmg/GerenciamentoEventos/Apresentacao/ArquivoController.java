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
import java.text.DateFormat;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author petronio
 */
@Named(value = "arquivoController")
@RequestScoped
public class ArquivoController
        extends ControllerBaseEntidade<Arquivo>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public ArquivoController() {
        
    }
    
    @EJB
    ArquivoRepositorio dao;
    
    UploadedFile arquivo;

    public UploadedFile getArquivo() {
        return arquivo;
    }

    public void setArquivo(UploadedFile arquivo) {
        this.arquivo = arquivo;
    }
    
    @Override
    public Arquivo getFiltro() {
        if(getSessao("filtro_nome") != null){
            filtro.setNome(getSessao("filtro_nome"));
        }
        
        if(getSessao("filtro_uri") != null){
            filtro.setUri(getSessao("filtro_uri"));
        }
        
        return filtro;
    }

    @Override
    public void setFiltro(Arquivo filtro) {
        this.filtro = filtro;
        if(filtro.getNome()!= null){
            setSessao("filtro_nome",filtro.getNome());
        }
        if(filtro.getUri()!= null){
            setSessao("filtro_uri",filtro.getUri());
        }
    }
    
    
    @PostConstruct
    public void init() {
        setRepositorio(dao);            
        setFiltro(new Arquivo());
    }

  

    @Override
    public void salvar() {
        
        SalvarEntidade();
        
        // atualiza a listagem
        filtrar();
    }

    @Override
    public String apagar() {
        if (dao.Apagar(getEntidade(), getConfiguracao("DIRETORIO_ARQUIVOS"))) {

            Mensagem("Sucesso", "Registro removido com sucesso!");
            AppendLog("Apagou a entidade " + getEntidade().getClass().getSimpleName() + " " + getEntidade().getId() + "(" + getEntidade().toString() + ")");
        } else {
            MensagemErro("Falha", "Registro não foi removido! Consulte o Log ou o administrador do sistema!");
            AppendLog("Falha ao remover a entidade " + getEntidade().getClass().getSimpleName() + " " + getEntidade().getId() + "(" + getEntidade().toString() + ")" + ": " + repositorio.getErro().getMessage());
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
   
    
    public void arquivoFileUpload() {  
        setEntidade(criaArquivo(arquivo));
        if(dao.Salvar(entidade)){
            Mensagem("Sucesso", "Arquivo anexado com êxito!");
            
        } else {
            Mensagem("Falha", "Falha ao anexar o arquivo!");
            
        }
        
    }

    
}
