/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao;



import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Inscricao;
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
@Named(value = "selecaoInscricoesEventoController")
@RequestScoped
public class SelecaoInscricoesEventoController
        extends ControllerBaseRelatorio<Inscricao>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public SelecaoInscricoesEventoController() {
        setArquivoSaida("ResultadoGeralSelecaoInscricoes");
        setRelatorio("Relatorios/ResultadoGeralSelecaoInscricoes.jasper");
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
            Logger.getLogger(SelecaoInscricoesEventoController.class.getName()).log(Level.SEVERE, null, ex);
            return new HashMap<>();
        }
    }

    @Override
    public List<Inscricao> getDados() {
                
        List<Inscricao> tmp = daoInscricao
                .IgualA("evento", getEvento())
                .Ordenar("ordem", "ASC")
                .Buscar();
        
        return tmp;
    }
}
