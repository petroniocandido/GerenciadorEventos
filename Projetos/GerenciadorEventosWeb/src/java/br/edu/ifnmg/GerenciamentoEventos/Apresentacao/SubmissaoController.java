/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao;



import br.edu.ifnmg.DomainModel.AreaConhecimento;
import br.edu.ifnmg.DomainModel.Arquivo;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseEntidade;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.GenericDataModel;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.InscricaoItem;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.InscricaoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.SubmissaoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Submissao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.SubmissaoStatus;
import java.io.IOException;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Isla Guedes
 */
@Named(value = "submissaoController")
@RequestScoped
public class SubmissaoController
    extends ControllerBaseEntidade<Submissao> implements Serializable {

    /**
     * Creates a new instance of SubmissaoController
     */
    public SubmissaoController() {        
    }
    
    @EJB
    SubmissaoRepositorio dao;
    
    @EJB
    InscricaoRepositorio daoInsc;
    
    AreaConhecimento areaConhecimento;
    
    String palavraChave;
    
    @Override
    public Submissao getFiltro() {
        if (filtro == null) {
            filtro = new Submissao();
            filtro.setTitulo(getSessao("sbctrl_titulo"));
            filtro.setAutor1(getSessao("sbctrl_autor1"));
            filtro.setAutor2(getSessao("sbctrl_autor2"));
        }
        return filtro;
    }

    @Override
    public void setFiltro(Submissao filtro) {
        this.filtro = filtro;
        if (filtro != null) {
            setSessao("sbctrl_titulo",filtro.getTitulo());
            setSessao("sbctrl_autor1",filtro.getAutor1());
            setSessao("sbctrl_autor2",filtro.getAutor2());
        }
    }

    @PostConstruct
    public void init() {
        setRepositorio(dao);
        setPaginaEdicao("editarSubmissao.xhtml");
        setPaginaListagem("listagemSubmissoes.xhtml");
    }
    
    InscricaoItem inscricaoItem;
    public InscricaoItem getInscricao(){
        if(inscricaoItem == null){
            inscricaoItem = (InscricaoItem) getSessao("inscricaoItem", daoInsc);
        }
        return inscricaoItem;
    }
    
    public String criaSubmissaoAtividade() {
         
        if(getInscricao() == null)
            return "inscricaoAtividade.xhtml";
         
        return "submissao.xhtml";
    }
    
    @Override
    public void limpar() {
        setEntidade(new Submissao());
    }
    
    public SubmissaoStatus[] getStatus() {
        return SubmissaoStatus.values();
    }
    
    public AreaConhecimento getAreaConhecimento() {
        return areaConhecimento;
    }
    
    @Override
    public void salvar(){
        
        if(entidade.getInscricao() == null)
            entidade.setInscricao(getInscricao());
        
        super.salvar();
        
      if(!entidade.hasAreaConhecimento())  {
          Mensagem("Falha", "Você precisa adicionar pelo menos uma Área de conhecimento!");
          return;
      }
      
      if(entidade.hasPalavrasChave())  {
          Mensagem("Falha", "Você precisa adicionar pelo menos uma Palavra-Chave!");
          return;
      }
      
      if(entidade.getInscricao() != null){
      
          if(!entidade.hasMinimoDeArquivos() ){
              Mensagem("Falha", "Você precisa adicionar arquivos!");
              return;
          }
          
          if(!entidade.hasMinimoDeAutores() ){
              Mensagem("Falha", "Você precisa adicionar autores!");
              return;
          }
      
      }
       
      
        setEntidade(entidade);
      
    }    
    
    public void setAreaConhecimento(AreaConhecimento areaConhecimento) {
        this.areaConhecimento = areaConhecimento;
    }
        
    public void addAreaConhecimento() {
        getEntidade().add(areaConhecimento);
        if(dao.Salvar(entidade)){
            Mensagem("Sucesso", "Área de conhecimento adicionada com êxito!");
        } else {
            Mensagem("Falha", "Falha ao adicionar Área de conhecimento!");
            AppendLog("Erro ao anexar a Área de conhecimento " + String.valueOf( areaConhecimento ) + " a submissão " + entidade + ":" + dao.getErro());
        }
        areaConhecimento = null; 
    }

    public void removeAreaConhecimento() {
        getEntidade().remove(areaConhecimento);
        dao.Salvar(entidade);
        areaConhecimento = null; 
    }

    public String getPalavraChave() {
        return palavraChave;
    }

    public void setPalavraChave(String palavraChave) {
        this.palavraChave = palavraChave;
    }
    
    public void addPalavraChave() {
        getEntidade().add(palavraChave);
        if(dao.Salvar(entidade)){
            Mensagem("Sucesso", "Palavra chave adicionada com êxito!");
        } else {
            Mensagem("Falha", "Falha ao adicionar palavra chave!");
            AppendLog("Erro ao anexar a palavra chave " + String.valueOf( palavraChave ) + " a submissão " + entidade + ":" + dao.getErro());
        }
        palavraChave = null; 
    }

    public void removePalavraChave() {
        getEntidade().remove(palavraChave);
        dao.Salvar(entidade);
        palavraChave = null; 
    }
    
    
    public void fileUploadListener(FileUploadEvent event) {  
        entidade = dao.Refresh(getEntidade());
        Arquivo tmp = criaArquivo(event.getFile());
        String arq = (String)event.getComponent().getAttributes().get("arquivo");
        switch(arq){
            case "Arquivo Identificado":
                entidade.setArquivo1(tmp);
                break;
            case "Arquivo Não Identificado":
                entidade.setArquivo2(tmp);
                break;
        }
        
        if(dao.Salvar(entidade)){
            Mensagem("Sucesso", "Arquivo anexado com êxito!");
            AppendLog("Anexou o arquivo " + tmp + " a submissão " + entidade);
        } else {
            Mensagem("Falha", "Falha ao anexar o arquivo!");
            AppendLog("Erro ao anexar o arquivo " + tmp + " a submissão " + entidade + ":" + dao.getErro());
        }        
    }
    
    public GenericDataModel getPublicoDataModel() {
        return new GenericDataModel<>(getPublicoListagem(), repositorio);
    }
    
    public List<Submissao> getPublicoListagem(){
        InscricaoItem inscricaoItem = (InscricaoItem) getSessao("inscricaoItem", daoInsc);
        if(inscricaoItem == null)
            return null;
        
        return dao.IgualA("inscricao", inscricaoItem).Buscar();
    }
    
     public void onPublicoRowSelect(SelectEvent event) {
        try {
            Submissao obj = (Submissao) event.getObject();
            setId(obj.getId());
            FacesContext.getCurrentInstance().getExternalContext().redirect("submissao.xhtml");
        } catch (IOException ex) {
            Mensagem("Falha", "Falha ao abrir submissão!");
            AppendLog("Falha ao abrir submissão:" + ex);
        }
    }
}

