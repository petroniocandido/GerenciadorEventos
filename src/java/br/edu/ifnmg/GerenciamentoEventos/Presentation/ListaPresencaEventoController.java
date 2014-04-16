/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Presentation;



import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Evento;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Inscricao;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Servicos.InscricaoRepositorio;
import br.edu.ifnmg.GerenciamentoEventos.Presentation.Comum.ControllerBaseRelatorio;
import javax.inject.Named;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author petronio
 */
@Named(value = "listaPresencaEventoController")
@SessionScoped
public class ListaPresencaEventoController
        extends ControllerBaseRelatorio<Inscricao>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public ListaPresencaEventoController() {
        setArquivoSaida("ListaPresencaEvento");
        setRelatorio("Relatorios/ListaPresencaEvento.jrxml");
    }
    
    @EJB
    InscricaoRepositorio daoInscricao;
            
    
        
    @Override
    protected Map<String, Object> carregaParametros() {
        try {
            return getParametrosComuns();
        } catch (MalformedURLException ex) {
            Logger.getLogger(ListaPresencaEventoController.class.getName()).log(Level.SEVERE, null, ex);
            return new HashMap<>();
        }
    }

    @Override
    public List<Inscricao> getDados() {
        Inscricao i = new Inscricao();
        i.setEvento(getEvento());
        
        List<Inscricao> tmp = daoInscricao.Buscar(i);
        
        return tmp;
    }
    
}
