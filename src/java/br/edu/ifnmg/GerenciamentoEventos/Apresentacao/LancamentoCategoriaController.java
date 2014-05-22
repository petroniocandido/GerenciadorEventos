/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao;



import br.edu.ifnmg.GerenciamentoEventos.DomainModel.LancamentoCategoria;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.LancamentoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseEntidade;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.GenericDataModel;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author petronio
 */
@Named(value = "lancamentoCategoriaController")
@SessionScoped
public class LancamentoCategoriaController
        extends ControllerBaseEntidade<LancamentoCategoria>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public LancamentoCategoriaController() {
        id = 0L;
        setEntidade(new LancamentoCategoria());
        setFiltro(new LancamentoCategoria());
    }
    
    @EJB
    LancamentoRepositorio dao;
    
    @PostConstruct
    public void init() {
        setRepositorio(dao);
    }

   
    @Override
    public void salvar() {
        Rastrear(entidade);

        // salva o objeto no BD
        if (dao.SalvarCategoria(entidade)) {

            setId(entidade.getId());

            Mensagem("Sucesso", "Registro salvo com sucesso!");
            AppendLog("Editou a entidade " + entidade.getClass().getSimpleName() + " " + entidade.getId() + "(" + entidade.toString() + ")");
        } else {
            MensagemErro("Falha", "Registro não foi salvo! Consulte o Log ou o administrador do sistema!");
            AppendLog("Falha ao editar a entidade " + entidade.getClass().getSimpleName() + " " + entidade.getId() + "(" + entidade.toString() + ")" + ": " + repositorio.getErro().getMessage());
        }
        // atualiza a listagem
        filtrar();
    }

    @Override
    public String apagar() {
        ApagarEntidade();
        filtrar();
        return "listagemLancamentoCategorias.xtml";
    }

    @Override
    public String abrir() {
        setEntidade(dao.AbrirCategoria(id));
        return "editarLancamentoCategoria.xhtml";
    }

    @Override
    public String cancelar() {
        return "listagemLancamentoCategorias.xhtml";
    }

    @Override
    public void limpar() {
        
        setEntidade(new LancamentoCategoria());
    }

    @Override
    public String novo() {
        limpar();
        return "editarLancamentoCategoria.xhtml";
    }

    
    @Override
    public GenericDataModel getDataModel(){
        LancamentoCategoriaDataModel dm = new LancamentoCategoriaDataModel(dao.BuscarCategorias(filtro),null);
        dm.setLancamentoRepositorio(dao);
        return dm;
    }
    
}
