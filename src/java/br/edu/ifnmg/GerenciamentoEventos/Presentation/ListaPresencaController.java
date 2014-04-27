/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Presentation;



import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Atividade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.InscricaoItem;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.InscricaoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Presentation.Comum.ControllerBaseRelatorio;
import javax.inject.Named;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author petronio
 */
@Named(value = "listaPresencaController")
@SessionScoped
public class ListaPresencaController
        extends ControllerBaseRelatorio<InscricaoItem>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public ListaPresencaController() {
        setArquivoSaida("ListaPresencaAtividade");
        setRelatorio("Relatorios/ListaPresencaAtividade.jrxml");
    }
    
    @EJB
    InscricaoRepositorio daoInscricao;
    
    Atividade atividade;

    
    @Override
    protected Map<String, Object> carregaParametros() {
        return new HashMap<>();
    }

    @Override
    public List<InscricaoItem> getDados() {
        InscricaoItem i = new InscricaoItem();
        i.setAtividade(atividade);
        return daoInscricao.Buscar(i);
    }

    public Atividade getAtividade() {
        return atividade;
    }

    public void setAtividade(Atividade atividade) {
        this.atividade = atividade;
    }
    
    
}
