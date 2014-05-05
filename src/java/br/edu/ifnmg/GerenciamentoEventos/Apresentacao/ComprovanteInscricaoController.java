/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.GerenciamentoEventos.Apresentacao;



import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Atividade;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.InscricaoItem;
import br.edu.ifnmg.GerenciamentoEventos.Aplicacao.ControllerBaseRelatorio;
import br.edu.ifnmg.GerenciamentoEventos.DomainModel.Inscricao;
import static br.edu.ifnmg.GerenciamentoEventos.DomainModel.InscricaoItem_.atividade;
import javax.inject.Named;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author petronio
 */
@Named(value = "comprovanteInscricaoController")
@SessionScoped
public class ComprovanteInscricaoController
        extends ControllerBaseRelatorio<InscricaoItem>
        implements Serializable {

    /**
     * Creates a new instance of FuncionarioBean
     */
    public ComprovanteInscricaoController() {
        setArquivoSaida("ComprovanteInscricao");
        setRelatorio("Relatorios/ComprovanteMatricula.jrxml");
    }
    
    Inscricao inscricao;

    
             
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
        List<InscricaoItem> tmp = inscricao.getItens();
        return tmp;
    }

    public Inscricao getInscricao() {
        return inscricao;
    }

    public void setInscricao(Inscricao inscricao) {
        this.inscricao = inscricao;
    }
    
    

}
