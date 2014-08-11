/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao.Relatorios;



import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.InscricaoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseRelatorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.InscricaoItem;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.InscricaoTipo;
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
@Named(value = "inscricoesAtividadeSinteseController")
@RequestScoped
public class InscricoesAtividadeSinteseController
        extends ControllerBaseRelatorio<InscricaoItem>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public InscricoesAtividadeSinteseController() {
        setArquivoSaida("ListaPresencaEvento");
        setRelatorio("InscricoesAtividadeSintese.jasper");
    }
    
    @EJB
    InscricaoRepositorio daoInscricao;
            
    @Override
    protected Map<String, Object> carregaParametros() {
        try {
            
            Map<String, Object> tmp = getParametrosComuns();
            tmp.put("data", new Date());
            return tmp;
        } catch (MalformedURLException ex) {
            Logger.getLogger(InscricoesAtividadeSinteseController.class.getName()).log(Level.SEVERE, null, ex);
            return new HashMap<>();
        }
    }

    @Override
    public List<InscricaoItem> getDados() {
                
        List<InscricaoItem> tmp = daoInscricao.getRepositorioItem()
                .IgualA("evento", getEvento())
                .IgualA("tipo", InscricaoTipo.InscricaoItem)
                .Join("atividade", "a")
                .Ordenar("a.nome", "ASC")
                .Buscar();
        
        return tmp;
    }
}
