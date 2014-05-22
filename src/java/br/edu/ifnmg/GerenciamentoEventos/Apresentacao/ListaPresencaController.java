/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao;



import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Atividade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.InscricaoItem;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.InscricaoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseRelatorio;
import javax.inject.Named;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author petronio
 */
@Named(value = "listaPresencaController")
@RequestScoped
public class ListaPresencaController
        extends ControllerBaseRelatorio<InscricaoItem>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public ListaPresencaController() {
        setArquivoSaida("ListaPresencaAtividade");
        setRelatorio("Relatorios/ListaPresencaAtividade.jasper");
    }
    
    @EJB
    InscricaoRepositorio daoInscricao;
    
    Atividade atividade;

    
             
    @Override
    protected Map<String, Object> carregaParametros() {
        try {
            
            Map<String, Object> tmp = getParametrosComuns();
            tmp.put("data", new Date());
            return tmp;
        } catch (MalformedURLException ex) {
            Logger.getLogger(ListaPresencaEventoController.class.getName()).log(Level.SEVERE, null, ex);
            return new HashMap<>();
        }
    }


    @Override
    public List<InscricaoItem> getDados() {
        InscricaoItem i = new InscricaoItem();
        i.setAtividade(atividade);
        return daoInscricao.getRepositorioItem()
                .IgualA("atividade", atividade)
                .Join("pessoa", "p")
                .Ordenar("p.nome", "ASC")
                .Buscar();
    }

    public Atividade getAtividade() {
        return atividade;
    }

    public void setAtividade(Atividade atividade) {
        this.atividade = atividade;
    }
    
    
}
